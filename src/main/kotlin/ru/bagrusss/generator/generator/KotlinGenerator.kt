package ru.bagrusss.generator.generator

import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.kotlin.model.KotlinClassModel
import ru.bagrusss.generator.kotlin.model.KotlinPrimitiveModel
import ru.bagrusss.generator.model.Model
import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.*
import ru.bagrusss.generator.kotlin.fields.*
import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.io.PrintWriter
import java.util.TreeSet

internal typealias ProtobufType = DescriptorProtos.FieldDescriptorProto.Type


class KotlinGenerator(private val input: InputStream,
                      private val output: PrintStream,
                      private val realmPath: String,
                      private val realmPackage: String,
                      private val prefix: String,
                      serializer: Serializer): Generator(serializer) {

    private var protoFilePackage = ""
    private var protoFileJavaPackage = ""

    private val OPTIONAL = DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL
    private val REPEATED = DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED

    private val packagesSet = TreeSet<String>()
    private val protoToJavaPackagesMap = HashMap<String, String>()
    private val mapsSet = TreeSet<String>()


    private fun writeClass(path: String, fileName: String, classBody: String) {
        val protoPackageDir = File(path)
        if (!protoPackageDir.exists()) {
            protoPackageDir.mkdir()
        }
        val file = File(path, fileName)
        file.createNewFile()
        PrintWriter(file).use {
            it.write(classBody)
        }
    }


    override fun filter(node: DescriptorProtos.DescriptorProto): Boolean {
        return !protoFileJavaPackage.contains("google", true)
                && !node.name.contains("Swift", true)
    }

    override fun generate() {
        Logger.prepare()

        val response = PluginProtos.CodeGeneratorResponse.newBuilder()
        val request = PluginProtos.CodeGeneratorRequest.parseFrom(input)

        listOf(Pair(INT, 0),
               Pair(LONG, 0L),
               Pair(FLOAT, "0f"),
               Pair(DOUBLE, 0.0),
               Pair(BOOLEAN, false),
               Pair(ClassName("kotlin", "String"), "\"\"")).forEach {
            val primitiveModel: Model = KotlinPrimitiveModel(realmPackage, prefix, it.first, it.second)
            val realmTypeFile = PluginProtos.CodeGeneratorResponse
                                            .File
                                            .newBuilder()
                                            .setName(primitiveModel.getFileName())
                                            .setContent(primitiveModel.getModelBody())
                                            .build()
            response.addFile(realmTypeFile)
        }



        request.protoFileList.forEach { protoFile ->
            protoFilePackage = protoFile.`package`
            protoFileJavaPackage = protoFile.options.javaPackage
            packagesSet.add(protoFilePackage)
            protoToJavaPackagesMap.put(protoFilePackage, protoFileJavaPackage)


            Logger.log("proto package java ${protoFile.options.javaPackage}")
            protoFile.messageTypeList.forEach {

                if (it.hasOptions() /*&& it.options.hasExtension(SwiftDescriptor.swiftMessageOptions)*/) {
                    //if (it.hasOptions() && it.options.hasField(SwiftDescriptor.SwiftFileOptions.getDescriptor().fields.first { it.jsonName.contains("generate_realm_object", true) })) {
                    parseCurrent(it)
                }
            }
        }

        response.build().writeTo(output)


    }

    private fun parseCurrent(node: DescriptorProtos.DescriptorProto, parentNameOriginal: String = "", parentNameRealm: String = "") {
        if (filter(node)) {
            val realmPackage = "$realmPackage.$protoFilePackage"
            val realmClassName = "${if (parentNameRealm.isNotEmpty()) parentNameRealm.replace(".", "") else prefix}${node.name}"
            val fullName = "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else ""}${node.name}"
            val protoFullName = "$protoFileJavaPackage.$fullName"

            node.nestedTypeList.forEach {
                parseCurrent(it, "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else "" }${node.name}", realmClassName)
            }

            if (node.fieldList.isNotEmpty()) {
                val isMap = node.options.mapEntry
                val classModelBuilder = KotlinClassModel.Builder(realmPackage, realmClassName, protoFullName)
                                                        .isMap(isMap)
                if (isMap)
                    mapsSet.add("$protoFilePackage.$fullName")

                Logger.log("generate $protoFullName, nodeName = ${node.name}")

                node.fieldList.forEach { field ->
                    val property = generateProperty(field)
                    classModelBuilder.addField(property)
                }

                val model: Model = classModelBuilder.build()

                writeClass("$realmPath${File.separator}$protoFilePackage", model.getFileName(), model.getModelBody())

            }

        }

    }

    private fun generateProperty(field: DescriptorProtos.FieldDescriptorProto): Field<*> {
        Logger.log("Field_ name=${field.name}, type=${field.typeName}, field=$field")

        val fieldBuilder = when (field.type) {
            ProtobufType.TYPE_INT32 -> IntField.newBuilder()
            ProtobufType.TYPE_INT64 -> LongField.newBuilder()
            ProtobufType.TYPE_FLOAT -> FloatField.newBuilder()
            ProtobufType.TYPE_DOUBLE -> DoubleField.newBuilder()
            ProtobufType.TYPE_STRING -> StringField.newBuilder()
            ProtobufType.TYPE_ENUM -> {
                val protoPackage = packagesSet.first { field.typeName.indexOf(it) == 1 }
                val clearTypeName =  field.typeName.substring(1).replace(protoPackage, "")
                val javaPackage = protoToJavaPackagesMap[protoPackage]
                EnumField.newBuilder().fullProtoTypeName("$javaPackage$clearTypeName")
            }
            ProtobufType.TYPE_BOOL -> BoolField.newBuilder()
            ProtobufType.TYPE_BYTES -> ByteArrayField.newBuilder()
            ProtobufType.TYPE_MESSAGE -> {
                val fullProtoName = field.typeName.substring(1)
                val builder = if (!mapsSet.contains(fullProtoName))
                                  MessageField.newBuilder()
                              else MapField.newBuilder()
                val protoPackage = if (field.typeName.indexOf(protoFilePackage) == 1)
                                       protoFilePackage
                                   else packagesSet.first { field.typeName.indexOf(it) == 1 }

                val clearedFullName =  field.typeName.substring(protoPackage.length + 1).replace(".", "")

                builder.fullProtoTypeName(clearedFullName)
                       .protoPackage("$protoPackage.")
            }

            else -> throw UnsupportedOperationException("name=${field.name}, type=${field.typeName}")
        }


        fieldBuilder.optional(field.label == OPTIONAL)
                    .repeated(field.label == REPEATED)
                    .fieldName(field.name)
                    .realmPackage(realmPackage)                                         //Just for maps
                    .primaryKey(field.hasOptions() /*|| field.name == "id"*/ || (field.name == "key" && field.type == ProtobufType.TYPE_STRING))
                    .prefix(prefix)


        return fieldBuilder.build()
    }
}