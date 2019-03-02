package ru.bagrusss.generator.react.kotlin

import com.google.protobuf.ExtensionRegistryLite
import com.squareup.kotlinpoet.FunSpec
import google.protobuf.DescriptorProtos
import google.protobuf.KotlinDescriptor
import google.protobuf.SwiftDescriptor
import google.protobuf.compiler.PluginProtos
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.Type
import ru.bagrusss.generator.generator.Generator
import ru.bagrusss.generator.generator.ProtobufType
import ru.bagrusss.generator.react.UtilsModelBuilder
import ru.bagrusss.generator.react.kotlin.field.*
import ru.bagrusss.generator.react.kotlin.model.KotlinReactModel
import ru.bagrusss.generator.react.params.ReactParams
import java.util.*

class KotlinReactGenerator(params: ReactParams): Generator<ReactParams>(params) {

    private lateinit var utilsBuilder: UtilsModelBuilder<FunSpec>

    private val valuesTypesMap = HashMap<String, String>()

    override fun generate() {
        Logger.log("react start")

        val extensionRegistry = ExtensionRegistryLite.newInstance()
        SwiftDescriptor.registerAllExtensions(extensionRegistry)
        KotlinDescriptor.registerAllExtensions(extensionRegistry)

        response = PluginProtos.CodeGeneratorResponse.newBuilder()
        request = PluginProtos.CodeGeneratorRequest.parseFrom(params.inputStream, extensionRegistry)

        utilsBuilder = KotlinUtilsModel.Builder()
                                       .fileName(params.className)
                                       .packageName(targetPackage)

        super.generate()

        Logger.log("react generated $count")

        writeFile(targetPath, "${params.className}.kt", utilsBuilder.build().body)
        Logger.log("maps and fields: $mapsValuesTypes")
        Logger.log("react end")
    }

    override fun filter(node: DescriptorProtos.DescriptorProto): Boolean {
        return true
    }

    private var count = 0

    override fun handleProtoMessage(message: DescriptorProtos.DescriptorProto) {
        if (filter(message)) {
            parseCurrent(message)
        }
    }

    private val mapsValuesTypes = TreeMap<String, Type>()

    private fun parseCurrent(node: DescriptorProtos.DescriptorProto, parentNameOriginal: String = "", generateReact: Boolean = false) {

        val fullName = "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else ""}${node.name}"
        val protoFullName = "$protoFileJavaPackage.$fullName"
        val needGenerate = node.options.hasExtension(SwiftDescriptor.swiftMessageOptions)
                && node.options.getExtension(SwiftDescriptor.swiftMessageOptions).generateReact

        node.nestedTypeList.forEach {
            parseCurrent(it, "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else "" }${node.name}", needGenerate || generateReact)
        }

        if (needGenerate || generateReact) {
            val isMap = node.options.mapEntry

            val modelBuilder = KotlinReactModel.Builder()
                                               .isMap(isMap)
                                               .protoClassFullName(protoFullName)

            if (isMap) {
                mapsSet.add(protoFullName)
                val valueField = node.fieldList.find { it.name == "value" }!!
                val valueType = getTypeName(valueField)
                if (valueType == Type.ENUM || valueType == Type.MESSAGE) {
                    val typeName = gerFullName(valueField)
                    valuesTypesMap[protoFullName] = typeName
                }
                mapsValuesTypes[protoFullName] = valueType
            }

            node.fieldList.forEach {
                val field = generateProperty(it)
                modelBuilder.addField(field)
            }


            val model = modelBuilder.build() as KotlinReactModel
            val functions = model.getMapFunctions()
            functions?.let { (toWritableMap, fromReadableMap) ->
                utilsBuilder.addFun(toWritableMap)
                            .addFun(fromReadableMap)
            }

            ++count
        }
    }

    override fun generateProperty(field: DescriptorProtos.FieldDescriptorProto): Field<*> {
        val fieldBuilder = when (field.type) {
            ProtobufType.TYPE_INT32,
            ProtobufType.TYPE_UINT32,
            ProtobufType.TYPE_FIXED32,
            ProtobufType.TYPE_SFIXED32 -> IntReactField.Builder()

            ProtobufType.TYPE_INT64,
            ProtobufType.TYPE_UINT64,
            ProtobufType.TYPE_FIXED64,
            ProtobufType.TYPE_SFIXED64 -> LongReactField.Builder()

            ProtobufType.TYPE_BOOL     -> BoolReactField.Builder()
            ProtobufType.TYPE_STRING   -> StringReactField.Builder()
            ProtobufType.TYPE_FLOAT    -> FloatReactField.Builder()
            ProtobufType.TYPE_DOUBLE   -> DoubleReactField.Builder()
            ProtobufType.TYPE_BYTES    -> BytesReactField.Builder()
            ProtobufType.TYPE_MESSAGE  -> {
                val fullName = gerFullName(field)
                val isMap = mapsSet.contains(fullName)
                val prototypeName: String?
                val builder = if (!isMap) {
                                  prototypeName = fullName
                                  MessageReactField.Builder()
                              } else {
                                  prototypeName = valuesTypesMap[fullName]
                                  MapReactField.Builder()
                                               .valueType(mapsValuesTypes[fullName]!!)
                              }
                builder.fullProtoTypeName(prototypeName?: "")

            }
            ProtobufType.TYPE_ENUM     -> EnumReactField.Builder()
                                                        .fullProtoTypeName(gerFullName(field))

            else                       -> StringReactField.Builder()
        }

        fieldBuilder.optional(field.label == OPTIONAL)
                    .repeated(field.label == REPEATED)
                    .fieldName(field.jsonName)

        return fieldBuilder.build()
    }

    private fun gerFullName(field: DescriptorProtos.FieldDescriptorProto): String {
        val protoPackage = packagesSet.first { field.typeName.indexOf(it) == 1 }
        val clearTypeName =  field.typeName
                                  .substring(1)
                                  .replace(protoPackage, "")

        val javaPackage = protoToJavaPackagesMap[protoPackage]

        return "$javaPackage$clearTypeName"
    }

    private fun getTypeName(field: DescriptorProtos.FieldDescriptorProto): Type {
        return when(field.type) {
            ProtobufType.TYPE_INT32,
            ProtobufType.TYPE_UINT32,
            ProtobufType.TYPE_FIXED32,
            ProtobufType.TYPE_SFIXED32   -> Type.INT

            ProtobufType.TYPE_INT64,
            ProtobufType.TYPE_UINT64,
            ProtobufType.TYPE_FIXED64,
            ProtobufType.TYPE_SFIXED64   -> Type.LONG

            ProtobufType.TYPE_BOOL       -> Type.BOOL
            ProtobufType.TYPE_FLOAT      -> Type.FLOAT
            ProtobufType.TYPE_DOUBLE     -> Type.DOUBLE
            ProtobufType.TYPE_MESSAGE    -> Type.MESSAGE
            ProtobufType.TYPE_ENUM       -> Type.ENUM
            else                         -> Type.STRING
        }
    }

}