package bagrusss.generator

import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.*
import java.io.File
import java.io.InputStream
import java.io.PrintStream

@Deprecated("shit code, but it works!")
class OldGenerator(private val input: InputStream,
                   private val output: PrintStream,
                   private val params: Array<String>) {

    private companion object {

        @JvmField var realmPackageName = "com.serenity.data_impl.realm.model"
        @JvmField var javaPackageName = ""
        @JvmField var protoPackageName = ""
        @JvmField val prefix = "Realm"

    }

    private fun generateRealmPrimitive(clazz: ClassName, defValue: Any): PluginProtos.CodeGeneratorResponse.File {
        val className = "$prefix${clazz.simpleName()}"

        val realmTypeFile = PluginProtos.CodeGeneratorResponse
                                        .File
                                        .newBuilder()
                                        .setName("$className.kt")

        val classBuilder = TypeSpec.classBuilder(ClassName.bestGuess(className))
        val fieldBuilder = PropertySpec.builder("value", clazz, KModifier.OPEN)
                                       .mutable(true)
                                       .initializer("%L", defValue)

        classBuilder.addProperty(fieldBuilder.build())
                    .addModifiers(KModifier.OPEN)
                    .superclass(ClassName.bestGuess("io.realm.RealmObject"))
                    .addFun(FunSpec.constructorBuilder().build())
                    .addFun(FunSpec.constructorBuilder()
                                   .addParameter(ParameterSpec.builder("value", clazz).build())
                                   .addStatement("this.value = value")
                                   .build())

        val content = KotlinFile.builder(realmPackageName, className)
                                .addType(classBuilder.build())
                                .build()
                                .toJavaFileObject()
                                .getCharContent(true)
                                .toString()

        realmTypeFile.content = content
        return realmTypeFile.build()
    }

    private fun parseCurrent(node: DescriptorProtos.DescriptorProto, response: PluginProtos.CodeGeneratorResponse.Builder, parentName: String = "") {

        if (/*(node.fieldList.isNotEmpty() || node.hasOptions()) && */!javaPackageName.contains("google", true) && !node.name.contains("Swift", true)) {
            Logger.log("parent=$parentName, current=${node.name}")
            val currentName = "${if (protoPackageName.contains("react", true)) "React" else ""}$prefix${parentName.replace(".", "")}${node.name}"
            Logger.log("current name=$currentName")
            if (node.fieldList.isNotEmpty()) {
                val outFile = PluginProtos.CodeGeneratorResponse
                                          .File
                                          .newBuilder()
                                          .setName("$currentName.kt")

                val classNameBuilder = TypeSpec.classBuilder(currentName)
                                               .addModifiers(KModifier.OPEN)
                                               .superclass(ClassName.bestGuess("io.realm.RealmObject"))

                val protoClass = "$protoPackageName.${if (parentName.isNotEmpty()) parentName + "." else ""}${node.name}"
                val classNameReturns = ClassName.bestGuess(protoClass)

                val toProtoMethodBuilder = FunSpec.builder("toProto")
                                                  .returns(classNameReturns)

                val toProtoBodyBuilder = StringBuilder().append("val p = ")
                                                        .append(protoPackageName)
                                                        .append('.')
                                                        .append(if (parentName.isNotEmpty()) parentName + "." else "")
                                                        .append(node.name)
                                                        .append(".newBuilder()\n")

                val realmProtoConstructor = FunSpec.constructorBuilder()
                                                   .addParameter("protoModel", ClassName.bestGuess(protoClass))

                val realmConstructorBodyBuilder = StringBuilder()

                node.fieldList.forEach { field ->
                    val generatedClass = generateField(field, node.name, toProtoBodyBuilder, realmConstructorBodyBuilder)
                    classNameBuilder.addProperty(generatedClass).build()
                }

                realmProtoConstructor.addStatement(realmConstructorBodyBuilder.toString())

                toProtoBodyBuilder.append("return p.build()")
                toProtoMethodBuilder.addStatement(toProtoBodyBuilder.toString())

                classNameBuilder.addFun(toProtoMethodBuilder.build())


                classNameBuilder.addFun(realmProtoConstructor.build())

                val realmDefaultConstructor = FunSpec.constructorBuilder()
                                                     .build()

                val className = classNameBuilder.addFun(realmDefaultConstructor)
                                                .build()

                outFile.content = KotlinFile.builder(realmPackageName, className.name!!)
                                            .addType(className)
                                            .build()
                                            .toJavaFileObject()
                                            .getCharContent(true)
                                            .toString()

                if (!response.fileBuilderList.contains(outFile))
                    response.addFile(outFile)
            }


            node.nestedTypeList.forEach {
                Logger.log("nested type = ${it.name} parent=${node.name}")
                parseCurrent(it, response, if (parentName.isNotEmpty()) parentName + "." + node.name else node.name)
            }
        }

    }

    private fun getRealmList(classType: ClassName,
                             protoFieldName: String,
                             realmFieldName: String,
                             toProtoBodyBuilder: StringBuilder?,
                             realmConstructorBodyBuilder: StringBuilder?,
                             nonPrimitiveName: String? = null): PropertySpec.Builder {
        val realmList = ClassName.bestGuess("io.realm.RealmList")
        val typedList = ParameterizedTypeName.get(realmList, classType)
        toProtoBodyBuilder?.let {
            it.append("p.addAll${realmFieldName.substring(0, 1).toUpperCase() + realmFieldName.substring(1)}(")
              .append(if (nonPrimitiveName == null) "$protoFieldName.map { it.value }" else "$protoFieldName.map { it.toProto() }")
              .append(")\n")
        }

        realmConstructorBodyBuilder?.let {
            it.append("$realmFieldName.addAll(protoModel.${protoFieldName}List.map { ${classType.simpleName()}(it)})\n")
        }

        return PropertySpec.builder(realmFieldName, typedList, KModifier.OPEN).apply {
            this.initializer("%L", "RealmList()")
        }
    }

    private fun getPrimitiveField(classType: ClassName,
                                  field: DescriptorProtos.FieldDescriptorProto,
                                  realmFieldName: String,
                                  toProtoBodyBuilder: StringBuilder?,
                                  realmConstructorBodyBuilder: StringBuilder?,
                                  defaultValue: Any): PropertySpec.Builder {
        toProtoBodyBuilder?.let {
            it.append("p.")
              .append(realmFieldName)
              .append(" = ")
              .append(realmFieldName)
              .append("\n")
        }

        realmConstructorBodyBuilder?.let {
            it.append(realmFieldName)
              .append(" = protoModel.")
              .append(realmFieldName)
              .append("\n")
        }


        return PropertySpec.builder(realmFieldName, classType, KModifier.OPEN).apply {
            if (field.hasOptions() || realmFieldName == "id" /*&& field.options.hasExtension(swiftFieldOptions )*/) {
                /*val ext = field.options.getExtension(SwiftDescriptor.swiftFieldOptions)
                if (ext.realmPrimaryKey)*/
                //val ext = field.options.getExtension(SwiftDescriptor.swiftFieldOptions)
                //val realmKey = ext.realmPrimaryKey
                this.addAnnotation(ClassName.bestGuess("io.realm.annotations.PrimaryKey"))
            }

            this.initializer("%L", defaultValue)

        }


    }

    private fun generateField(field: DescriptorProtos.FieldDescriptorProto,
                              parentName: String = "",
                              toProtoBodyBuilder: StringBuilder,
                              realmConstructorBodyBuilder: StringBuilder): PropertySpec {
        val fieldName = when (field.jsonName) {
            "package" -> "_package"
            else -> field.jsonName
        }

        val splitted = field.typeName.split(".")

        Logger.log("generate json=${field.typeName} ${field.type.name} jsonName=${field.jsonName}")
        val typeSpec = when ((field.type)) {
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32 -> {
                getPrimitiveField(INT, field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, 0)
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64 -> {

                getPrimitiveField(LONG, field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, 0L)
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT -> {

                getPrimitiveField(FLOAT, field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, "0f")
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE -> {
                getPrimitiveField(DOUBLE, field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, 0.0)
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING -> {
                if (field.label == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED) {
                    val classType = "RealmString"
                    val typedClass = ClassName.bestGuess(classType)

                    getRealmList(typedClass, field.name, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder)
                } else {
                    getPrimitiveField(ClassName.bestGuess("kotlin.String"), field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, "\"\"")
                }
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES -> {
                toProtoBodyBuilder.append("p.")
                                  .append(field.name)
                                  .append(" = io.protostuff.ByteString.copyFrom($fieldName);\n")

                realmConstructorBodyBuilder?.let {
                    it.append(fieldName)
                      .append(" = protoModel.")
                      .append(field.name)
                      .append(".toByteArray()\n")
                }

                PropertySpec.builder(fieldName, ClassName.bestGuess("kotlin.ByteArray"), KModifier.OPEN)
                            .initializer("%L", "ByteArray(0)")
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL -> {
                if (field.label == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED) {
                    val classType = "RealmBoolean"
                    val typedClass = ClassName.bestGuess(classType)

                    getRealmList(typedClass, field.name, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder)
                } else {
                    getPrimitiveField(BOOLEAN, field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, false)
                }
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM -> {
                toProtoBodyBuilder?.let {
                    val packageName = if (field.typeName.contains("proto", true)) "ProtoApi" else protoPackageName
                    it.append("p.")
                      .append(field.name)
                      .append(" = ")
                      .append(packageName)
                      .append('.')
                      .append(field.typeName.substring(field.typeName.indexOf(splitted[2])))
                      .append(".valueOf($fieldName)\n")
                }

                realmConstructorBodyBuilder?.let {
                    it.append(fieldName)
                      .append(" = protoModel.")
                      .append(field.name)
                      .append(".number;\n")
                }

                PropertySpec.builder(fieldName, Int::class.java, KModifier.OPEN).apply {
                    this.initializer("%L", -1)
                }
            }

            else -> {
                if (field.type == DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE) {
                    var customTypeName = "$prefix${field.typeName.replace(protoPackageName, "").replace(".", "")}"
                    Logger.log("custom type parent = $parentName, customType=$customTypeName\n")

                    if (field.label == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED) {
                        customTypeName = if (field.typeName.contains("react", true)) "React" + customTypeName else customTypeName
                        val typedClass = ClassName.bestGuess(customTypeName)

                        getRealmList(typedClass, field.name, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, "${if (field.typeName.contains("proto", true)) "ProtoApi" else protoPackageName}.${field.typeName.substring(field.typeName.indexOf(splitted[2]))}")
                    } else {
                        customTypeName = if (field.typeName.contains("react", true)) "React" + customTypeName else customTypeName
                        //поле с кастомным типом
                        val builder = PropertySpec.builder(fieldName, ClassName.bestGuess(customTypeName), KModifier.OPEN)

                        toProtoBodyBuilder?.let {
                            when (field.label) {
                                DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL -> {
                                    it.append("${field.name}?.let {\n")
                                      .append("\tp.")
                                      .append(field.name)
                                      .append(" = ")
                                      .append("it.toProto()\n}\n")
                                    builder.nullable(true)
                                           .initializer("%L", "null")
                                }
                                DescriptorProtos.FieldDescriptorProto.Label.LABEL_REQUIRED -> {
                                    it.append("p.")
                                      .append(field.name)
                                      .append(" = ")
                                      .append(field.name)
                                      .append(".toProto()\n")
                                    builder.addModifiers(KModifier.LATEINIT)
                                }
                                else -> {

                                }
                            }

                        }

                        realmConstructorBodyBuilder?.let {
                            when (field.label) {
                                DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL -> {
                                    it.append("if (protoModel.has")
                                      .append(field.name.substring(0, 1).toUpperCase() + field.name.substring(1))
                                      .append("())\n \t")
                                      .append(field.name)
                                      .append(" = ")
                                      .append(customTypeName)
                                      .append("(protoModel.${field.name})\n\n")
                                }
                                DescriptorProtos.FieldDescriptorProto.Label.LABEL_REQUIRED -> {
                                    it.append(field.name)
                                      .append(" = ")
                                      .append(customTypeName)
                                      .append("(protoModel.${field.name})\n")
                                }
                                else -> {

                                }
                            }
                        }
                        builder
                    }

                } else PropertySpec.builder(fieldName, String::class.java, KModifier.OPEN)
            }
        }

        typeSpec.mutable(true)

        /* // TODO написать генерацию аннотация для realm
         if (fieldName == "id") {
             typeSpec.addAnnotation(ClassName.bestGuess("io.realm.annotations.PrimaryKey"))
         }*/
        return typeSpec.build()
    }

    fun generate() {
        Logger.prepare()
        Logger.log("params:")
        params.forEach {
            Logger.log(it)
        }
        val realmPath = System.getenv()["realm_package"]
        realmPackageName = realmPath!!.replace("/", ".")

        val response = PluginProtos.CodeGeneratorResponse.newBuilder()
        val request = PluginProtos.CodeGeneratorRequest.parseFrom(input)

        response.addFile(generateRealmPrimitive(INT, 0))
        response.addFile(generateRealmPrimitive(LONG, 0L))
        response.addFile(generateRealmPrimitive(FLOAT, "0f"))
        response.addFile(generateRealmPrimitive(DOUBLE, 0.0))
        response.addFile(generateRealmPrimitive(ClassName("kotlin", "String"), "\"\""))
        response.addFile(generateRealmPrimitive(BOOLEAN, false))

        request.protoFileList.forEach { protoFile ->
            javaPackageName = protoFile.options.javaPackage
            protoPackageName = protoFile.`package`
            Logger.log("proto package ${protoFile.`package`}")
            protoFile.messageTypeList.forEach {
                if (it.hasOptions() /*&& it.options.hasExtension(SwiftDescriptor.swiftMessageOptions)*/) {
                    //if (it.hasOptions() && it.options.hasField(SwiftDescriptor.SwiftFileOptions.getDescriptor().fields.first { it.jsonName.contains("generate_realm_object", true) })) {
                    parseCurrent(it, response)
                    Logger.log("proto full name ${it.name}")
                }
            }
        }
        response.build().writeTo(output)
    }

}