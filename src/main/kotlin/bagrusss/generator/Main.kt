package bagrusss.generator

import bagrusss.generator.fields.ByteArrayField
import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption


/**
 * Created by bagrusss on 10.04.17
 */

object Main {

    @JvmField val packageName = "com.serenity.data_impl.realm.model"
    @JvmField var protoPackageName = ""
    @JvmField var protoFilePackage = ""
    @JvmField val prefix = "Realm"

    @JvmField val logPath = System.getProperty("user.dir") + "/log.txt"

    @JvmStatic
    fun generateRealmPrimitive(clazz: ClassName, defValue: Any): PluginProtos.CodeGeneratorResponse.File {
        val realmTypeFile = PluginProtos.CodeGeneratorResponse.File.newBuilder().setName("$prefix${clazz.simpleName()}.kt")


        val classBuilder = TypeSpec.classBuilder(ClassName.bestGuess("$prefix${clazz.simpleName()}"))
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
        //return classBuilder.build()
        val content = KotlinFile.builder(packageName, "$prefix${clazz.simpleName()}")
                .addType(classBuilder.build())
                .build()
                .toJavaFileObject()
                .getCharContent(true)
                .toString()

        realmTypeFile.content = content
        return realmTypeFile.build()
    }

    @JvmStatic
    fun main(args: Array<String>) {


        val log = File(logPath)
        log.delete()
        log.createNewFile()
        log("args: \n")
        args.forEach {
            log(it + '\n')
        }

        val ba = ByteArrayField.Builder()
        val ss = ba.build()

        oldGenerator()
    }

    @JvmStatic
    fun newGenerator() {
        val response = PluginProtos.CodeGeneratorResponse.newBuilder()
        val request = PluginProtos.CodeGeneratorRequest.parseFrom(System.`in`)

        response.addFile(generateRealmPrimitive(INT, 0))
        response.addFile(generateRealmPrimitive(LONG, 0L))
        response.addFile(generateRealmPrimitive(FLOAT, "0f"))
        response.addFile(generateRealmPrimitive(DOUBLE, 0.0))
        response.addFile(generateRealmPrimitive(ClassName("kotlin", "String"), "\"\""))
        response.addFile(generateRealmPrimitive(BOOLEAN, false))

        request.protoFileList.forEach { protoFile ->
            protoPackageName = protoFile.options.javaPackage
            protoFile.messageTypeList.forEach {
                if (it.hasOptions()) {
                    if (/*(node.fieldList.isNotEmpty() || node.hasOptions()) && */!protoPackageName.contains("google", true) && !it.name.contains("Swift", true)) {

                    }
                }
            }
        }
        response.build().writeTo(System.out)
    }

    @JvmStatic
    fun oldGenerator() {
        val response = PluginProtos.CodeGeneratorResponse.newBuilder()
        val request = PluginProtos.CodeGeneratorRequest.parseFrom(System.`in`)

        response.addFile(generateRealmPrimitive(INT, 0))
        response.addFile(generateRealmPrimitive(LONG, 0L))
        response.addFile(generateRealmPrimitive(FLOAT, "0f"))
        response.addFile(generateRealmPrimitive(DOUBLE, 0.0))
        response.addFile(generateRealmPrimitive(ClassName("kotlin", "String"), "\"\""))
        response.addFile(generateRealmPrimitive(BOOLEAN, false))

        request.protoFileList.forEach { protoFile ->
            protoPackageName = protoFile.options.javaPackage
            protoFilePackage = protoFile.`package`
            log("proto package ${protoFile.`package`}")
            protoFile.messageTypeList.forEach {
                if (it.hasOptions() /*&& it.options.hasExtension(SwiftDescriptor.swiftMessageOptions)*/) {
                    //if (it.hasOptions() && it.options.hasField(SwiftDescriptor.SwiftFileOptions.getDescriptor().fields.first { it.jsonName.contains("generate_realm_object", true) })) {
                    parseCurrent(it, response)
                    log("proto full name ${it.name}")
                }
            }
        }
        response.build().writeTo(System.out)
    }

    @JvmStatic
    fun parseCurrent(node: DescriptorProtos.DescriptorProto, response: PluginProtos.CodeGeneratorResponse.Builder, parentName: String = "") {

        if (/*(node.fieldList.isNotEmpty() || node.hasOptions()) && */!protoPackageName.contains("google", true) && !node.name.contains("Swift", true)) {
            log("parent=$parentName, current=${node.name}")
            val currentName = "${if (protoPackageName.contains("react", true)) "React" else ""}$prefix${parentName.replace(".", "")}${node.name}"
            log("current name=$currentName")
            if (node.fieldList.isNotEmpty()) {
                val outFile = PluginProtos.CodeGeneratorResponse.File.newBuilder().setName("$currentName.kt")

                val classNameBuilder = TypeSpec.classBuilder(currentName)
                        .addModifiers(KModifier.OPEN)
                        .superclass(ClassName.bestGuess("io.realm.RealmObject"))

                val classNameReturns = ClassName.bestGuess("$protoPackageName.${if (parentName.isNotEmpty()) parentName + "." else ""}${node.name}")

                val toProtoMethodBuilder = FunSpec.builder("toProto")
                        .returns(classNameReturns)

                val toProtoBodyBuilder = StringBuilder().append("val p = ")
                        .append(protoPackageName)
                        .append('.')
                        .append(if (parentName.isNotEmpty()) parentName + "." else "")
                        .append(node.name)
                        .append(".newBuilder()\n")

                val realmProtoConstructor = FunSpec.constructorBuilder()
                        .addParameter("protoModel", ClassName.bestGuess("$protoPackageName.${if (parentName.isNotEmpty()) parentName + "." else ""}${node.name}"))

                val realmConstructorBodyBuilder = StringBuilder()



                node.fieldList.forEach { field ->
                    val generatedClass = generateField(field, node.name, node.nestedTypeList, response, toProtoBodyBuilder, realmConstructorBodyBuilder)
                    classNameBuilder.addProperty(generatedClass).build()
                }

                realmProtoConstructor.addStatement(realmConstructorBodyBuilder.toString())

                toProtoBodyBuilder.append("return p.build()")
                toProtoMethodBuilder.addStatement(toProtoBodyBuilder.toString())

                classNameBuilder.addFun(toProtoMethodBuilder.build())


                classNameBuilder.addFun(realmProtoConstructor.build())

                val realmDefaultConstructor = FunSpec.constructorBuilder()
                        .build()

                val className = classNameBuilder.addFun(realmDefaultConstructor).build()

                val javaFile = KotlinFile.builder(packageName, className.name!!).addType(className).build()
                outFile.content = javaFile.toJavaFileObject().getCharContent(true).toString()
                if (!response.fileBuilderList.contains(outFile))
                    response.addFile(outFile)
            }



            node.nestedTypeList.forEach {
                log("nested type = ${it.name} parent=${node.name}")
                parseCurrent(it, response, if (parentName.isNotEmpty()) parentName + "." + node.name else node.name)
            }
        }

    }

    @JvmStatic
    fun getRealmList(classType: ClassName,
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

    @JvmStatic
    fun getPrimitiveField(classType: ClassName,
                          field: DescriptorProtos.FieldDescriptorProto,
                          realmFieldName: String,
                          toProtoBodyBuilder: StringBuilder?,
                          realmConstructorBodyBuilder: StringBuilder?,
                          label: DescriptorProtos.FieldDescriptorProto.Label,
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

    @JvmStatic
    fun log(string: String) {
        Files.write(Paths.get(logPath), "$string\n".toByteArray(), StandardOpenOption.APPEND)
    }

    @JvmStatic
    fun generateField(field: DescriptorProtos.FieldDescriptorProto,
                      parentName: String = "",
                      nestedTypes: List<DescriptorProtos.DescriptorProto>? = null,
                      response: PluginProtos.CodeGeneratorResponse.Builder? = null,
                      toProtoBodyBuilder: StringBuilder? = null,
                      realmConstructorBodyBuilder: StringBuilder? = null): PropertySpec {
        val fieldName = when (field.jsonName) {
            "package" -> "_package"
            else -> field.jsonName
        }

        var typeName = field.typeName
        val splitted = typeName.split(".")
        val lastItem = splitted.last()

        val convertedTypeName = if (lastItem.isEmpty()) typeName else lastItem
        log("generate json=${field.typeName} ${field.type.name} jsonName=${field.jsonName}")
        val typeSpec = when ((field.type)) {
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32 -> {
                getPrimitiveField(INT, field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, field.label, 0)
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64 -> {

                getPrimitiveField(LONG, field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, field.label, 0L)
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT -> {

                getPrimitiveField(FLOAT, field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, field.label, "0f")
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE -> {
                getPrimitiveField(DOUBLE, field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, field.label, 0.0)
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING -> {
                if (field.label == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED) {
                    val classType = "RealmString"
                    val typedClass = ClassName.bestGuess(classType)

                    //FieldSpec.builder(typedList, fieldName, Modifier.PUBLIC)
                    getRealmList(typedClass, field.name, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder)
                } else {
                    getPrimitiveField(ClassName.bestGuess("kotlin.String"), field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, field.label, "\"\"")
                }
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES -> {
                toProtoBodyBuilder?.let {
                    it.append("p.")
                            .append(field.name)
                            .append(" = io.protostuff.ByteString.copyFrom($fieldName);\n")
                }

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
                    getPrimitiveField(BOOLEAN, field, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, field.label, false)
                }
            }

            DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM -> {
                //val hasType = parent?.enumTypeList?.find { it.name.contains(convertedTypeName, true) || field.type.name.contains(it.name, true) }
                toProtoBodyBuilder?.let {
                    val packageName = if (field.typeName.contains("proto", true)) "ru.rocketbank.protomodel.api" else protoPackageName
                    it.append("p.")
                            .append(field.name)
                            .append(" = ")
                            .append(packageName)
                            .append('.')
                            .append(field.typeName.substring(field.typeName.indexOf(splitted[2])))
                            //.append(if (hasType != null) "${parentName.replace(prefix, "")}.$convertedTypeName" else "$protoPackageName.$convertedTypeName")
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
                    var customTypeName = "$prefix${field.typeName.replace(protoFilePackage, "").replace(".", "")}"
                    log("custom type parent = $parentName, customType=$customTypeName\n")

                    if (field.label == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED) {
                        customTypeName = if (field.typeName.contains("react", true)) "React" + customTypeName else customTypeName
                        val typedClass = ClassName.bestGuess(customTypeName)

                        getRealmList(typedClass, field.name, fieldName, toProtoBodyBuilder, realmConstructorBodyBuilder, "${if (field.typeName.contains("proto", true)) "ru.rocketbank.protomodel.api" else protoPackageName}.${field.typeName.substring(field.typeName.indexOf(splitted[2]))}")
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

}