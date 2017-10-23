package ru.bagrusss.generator.react.kotlin

import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.FunSpec
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.generator.Generator
import ru.bagrusss.generator.react.UtilsModelBuilder
import ru.bagrusss.generator.react.kotlin.field.DoubleReactField
import ru.bagrusss.generator.react.kotlin.field.IntReactField
import ru.bagrusss.generator.react.kotlin.model.KotlinReactModel
import ru.bagrusss.generator.realm.ProtobufType
import java.io.InputStream
import java.io.PrintStream

class KotlinReactGenerator(input: InputStream,
                           output: PrintStream,
                           private val reactPath: String): Generator(input, output) {

    private lateinit var utilsBuilder: UtilsModelBuilder<FunSpec>

    override fun generate() {
        Logger.log("start")

        response = PluginProtos.CodeGeneratorResponse.newBuilder()
        request = PluginProtos.CodeGeneratorRequest.parseFrom(input)


        utilsBuilder = KotlinUtilsModel.Builder()
                                       .fileName("ConvertUtils")
                                       .packageName("ru.rocketbank.serenity.react.utils")

        super.generate()

        val body = utilsBuilder.build().getBody()

        writeFile(reactPath, "ConvertUtils.kt", body)

    }

    override fun filter(node: DescriptorProtos.DescriptorProto): Boolean {
        return !protoFileJavaPackage.contains("google", true)
                && !node.name.contains("Swift", true)
    }


    override fun handleProtoMessage(message: DescriptorProtos.DescriptorProto) {
        if (message.hasOptions() && filter(message)) {
            Logger.log("parseCurrent()")
            parseCurrent(message)
        }
    }

    private fun parseCurrent(node: DescriptorProtos.DescriptorProto, parentNameOriginal: String = "") {
        val fullName = "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else ""}${node.name}"
        val protoFullName = "$protoFileJavaPackage.$fullName"

        node.nestedTypeList.forEach {
            parseCurrent(it, "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else "" }${node.name}")

            if (node.fieldList.isNotEmpty()) {
                val isMap = node.options.mapEntry
                val modelBuilder = KotlinReactModel.Builder()
                                                   .isMap(isMap)
                                                   .protoClassFullName(protoFullName)
                node.fieldList.forEach {
                    val field = generateProperty(it)
                    modelBuilder.addField(field)
                }

                if (isMap)
                    mapsSet.add("$protoFilePackage.$fullName")

                val model = modelBuilder.build() as KotlinReactModel
                val functions = model.getMapFunctions()

                utilsBuilder.addToWritableMapFun(functions.first)
                utilsBuilder.addFromReadableMapFun(functions.second)
            }
        }
    }

    override fun generateProperty(field: DescriptorProtos.FieldDescriptorProto): Field<*> {
        val fieldBuilder = when (field.type) {
            ProtobufType.TYPE_INT32 -> IntReactField.Builder()
            else -> DoubleReactField.Builder()
        }

        fieldBuilder.optional(field.label == OPTIONAL)
                    .repeated(field.label == REPEATED)
                    .fieldName(field.name)

        return fieldBuilder.build()
    }

}