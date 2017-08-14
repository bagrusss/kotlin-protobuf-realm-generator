package bagrusss.generator.generator

import bagrusss.generator.Logger
import bagrusss.generator.fields.Field
import bagrusss.generator.kotlin.fields.*
import bagrusss.generator.kotlin.model.KotlinClassModel
import bagrusss.generator.kotlin.model.KotlinPrimitiveModel
import bagrusss.generator.model.Model
import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.*
import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.io.PrintWriter
import java.util.TreeSet

class KotlinGenerator(private val input: InputStream,
                      private val output: PrintStream,
                      private val realmPath: String,
                      private val realmPackage: String,
                      private val prefix: String): Generator() {

    private companion object {
        @JvmField var protoFilePackage = ""

        @JvmField val OPTIONAL = DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL
        @JvmField val REPEATED = DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED
        @JvmField val REQUIRED = DescriptorProtos.FieldDescriptorProto.Label.LABEL_REQUIRED
    }

    private val packagesSet = TreeSet<String>()

    private fun writeClass(path: String, fileName: String, classBody: String) {
        val file = File(path, fileName)
        file.createNewFile()
        PrintWriter(file).use {
            it.write(classBody)
        }
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
            packagesSet.add(protoFilePackage)


            Logger.log("proto package ${protoFile.`package`}")
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
        filter = {!protoFilePackage.contains("google", true) && !node.name.contains("Swift", true)}
        if (filter.invoke()) {
            Logger.log("parent=$parentNameRealm, current=${node.name}")
            val realmPackage = "$realmPackage.${protoFilePackage}"
            val className = "${if (parentNameRealm.isNotEmpty()) parentNameRealm.replace(".", "") else prefix}${node.name}"
            val protoFullName = "${protoFilePackage}.${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else ""}${node.name}"

            node.nestedTypeList.forEach {
                Logger.log("nested type = ${it.name} parent=${node.name}")
                parseCurrent(it, "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else "" }${node.name}", className)
            }

            val protoPackageDir = File("$realmPath${File.separator}${protoFilePackage}")
            if (!protoPackageDir.exists()) {
                protoPackageDir.mkdir()
            }

            if (node.fieldList.isNotEmpty()) {
                val classModelBuilder = KotlinClassModel.Builder(realmPackage, className, protoFullName)

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

        val fieldBuilder = when (field.type) {
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32 -> IntField.Builder()
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64 -> LongField.Builder()
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT -> FloatField.Builder()
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE -> DoubleField.Builder()
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING -> StringField.Builder()
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM -> EnumField.Builder().fullProtoTypeName(field.typeName.substring(1))
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL -> BoolField.Builder()
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES -> ByteArrayField.Builder()
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE -> {
                val builder = MessageField.Builder()
                val protoPackage = if (field.typeName.contains(protoFilePackage)) {
                    protoFilePackage
                                   } else {
                                       packagesSet.first { field.typeName.contains(it) }
                                   }
                val clearedFullName =  field.typeName.substring(protoPackage.length + 1).replace(".", "")

                builder.fullProtoTypeName(clearedFullName)
                       .protoPackage("$protoPackage.")
            }

            else -> BoolField.Builder()
        }


        fieldBuilder.optional(field.label == OPTIONAL)
                    .repeated(field.label == REPEATED)
                    .fieldName(field.name)
                    .realmPackage(realmPackage)
                    .primaryKey(field.hasOptions())
                    .prefix(prefix)


        return fieldBuilder.build()
    }
}