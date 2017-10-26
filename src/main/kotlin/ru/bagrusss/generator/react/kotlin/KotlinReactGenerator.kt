package ru.bagrusss.generator.react.kotlin

import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.FunSpec
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.Type
import ru.bagrusss.generator.generator.Generator
import ru.bagrusss.generator.react.UtilsModelBuilder
import ru.bagrusss.generator.react.kotlin.field.*
import ru.bagrusss.generator.react.kotlin.model.KotlinReactModel
import ru.bagrusss.generator.realm.ProtobufType
import java.io.InputStream
import java.io.PrintStream
import java.util.*

class KotlinReactGenerator(input: InputStream,
                           output: PrintStream,
                           private val reactPath: String): Generator(input, output) {

    private lateinit var utilsBuilder: UtilsModelBuilder<FunSpec>

    private val valuesTypesMap = HashMap<String, String>()

    override fun generate() {
        Logger.log("react start")

        response = PluginProtos.CodeGeneratorResponse.newBuilder()
        request = PluginProtos.CodeGeneratorRequest.parseFrom(input)


        utilsBuilder = KotlinUtilsModel.Builder()
                                       .fileName("ConvertUtils")
                                       .packageName("ru.rocketbank.serenity.react.utils")

        super.generate()

        Logger.log("react generated $count")


        val body = utilsBuilder.build().getBody()

        writeFile(reactPath, "ConvertUtils.kt", body)
        Logger.log("maps and fields: $mapsValuesTypes")
        Logger.log("react end")
    }

    override fun filter(node: DescriptorProtos.DescriptorProto): Boolean {
        return !protoFileJavaPackage.contains("google", true)
                && !node.name.contains("Swift", true)
    }

    private var count = 0

    override fun handleProtoMessage(message: DescriptorProtos.DescriptorProto) {
        if (message.hasOptions()) {
            //Logger.log("${message.name} generate_react_object = ${message.options.descriptorForType.fields }")
            parseCurrent(message)
        }
    }

    private val mapsValuesTypes = TreeMap<String, Type>()

    private fun parseCurrent(node: DescriptorProtos.DescriptorProto, parentNameOriginal: String = "") {
        val fullName = "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else ""}${node.name}"
        val protoFullName = "$protoFileJavaPackage.$fullName"

        node.nestedTypeList.forEach {
            parseCurrent(it, "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else "" }${node.name}")
        }

        val isMap = node.options.mapEntry

        if (node.fieldList.isNotEmpty()) {
            val modelBuilder = KotlinReactModel.Builder()
                                               .isMap(isMap)
                                               .protoClassFullName(protoFullName)

            if (isMap) {
                mapsSet.add(protoFullName)
                val valueField = node.fieldList.find { it.name == "value" }!!
                val valueType = getTypeName(valueField)
                if (valueType == Type.ENUM || valueType == Type.MESSAGE) {
                    val typeName = gerFullName(valueField)
                    valuesTypesMap.put(protoFullName, typeName)
                }
                mapsValuesTypes.put(protoFullName, valueType)
            }

            node.fieldList.forEach {
                val field = generateProperty(it)
                modelBuilder.addField(field)
            }


            val model = modelBuilder.build() as KotlinReactModel
            val functions = model.getMapFunctions()
            functions?.let {
                utilsBuilder.addFun(it.first)
                utilsBuilder.addFun(it.second)
            }

            ++count
        }
    }

    override fun generateProperty(field: DescriptorProtos.FieldDescriptorProto): Field<*> {
        val fieldBuilder = when (field.type) {
            ProtobufType.TYPE_INT32     -> IntReactField.Builder()
            ProtobufType.TYPE_INT64     -> LongReactField.Builder()
            ProtobufType.TYPE_BOOL      -> BoolReactField.Builder()
            ProtobufType.TYPE_STRING    -> StringReactField.Builder()
            ProtobufType.TYPE_FLOAT     -> FloatReactField.Builder()
            ProtobufType.TYPE_DOUBLE    -> DoubleReactField.Builder()
            ProtobufType.TYPE_BYTES     -> BytesReactField.Builder()
            ProtobufType.TYPE_MESSAGE   -> {
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
            ProtobufType.TYPE_ENUM      -> EnumReactField.Builder()
                                                         .fullProtoTypeName(gerFullName(field))

            else                        -> StringReactField.Builder()
        }

        fieldBuilder.optional(field.label == OPTIONAL)
                    .repeated(field.label == REPEATED)
                    .fieldName(field.name)

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
            ProtobufType.TYPE_INT32     -> Type.INT
            ProtobufType.TYPE_INT64     -> Type.LONG
            ProtobufType.TYPE_BOOL      -> Type.BOOL
            ProtobufType.TYPE_FLOAT     -> Type.FLOAT
            ProtobufType.TYPE_DOUBLE    -> Type.DOUBLE
            ProtobufType.TYPE_MESSAGE   -> Type.MESSAGE
            ProtobufType.TYPE_ENUM      -> Type.ENUM
            else                        -> Type.STRING
        }
    }

}