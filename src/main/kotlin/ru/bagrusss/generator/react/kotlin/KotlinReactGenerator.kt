package ru.bagrusss.generator.react.kotlin

import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.generator.Generator
import java.io.InputStream
import java.io.PrintStream

class KotlinReactGenerator(input: InputStream,
                           output: PrintStream,
                           private val reactPath: String): Generator(input, output) {

    override fun generate() {
        Logger.prepare()

        val response = PluginProtos.CodeGeneratorResponse.newBuilder()
        val request = PluginProtos.CodeGeneratorRequest.parseFrom(input)

        val utilsBuilder = KotlinUtilsModel.Builder()
                                           .fileName("ConvertUtils")
                                           .packageName("ru.rocketbank.serenity.react.utils")

        request.protoFileList.forEach { protoFile ->

        }

        utilsBuilder.build().getBody()

    }

    override fun filter(node: DescriptorProtos.DescriptorProto): Boolean {
        return !protoFileJavaPackage.contains("google", true)
                && !node.name.contains("Swift", true)
    }

}