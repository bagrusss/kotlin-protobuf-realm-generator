package ru.bagrusss.generator.generator

import ru.bagrusss.generator.kotlin.model.KotlinPrimitiveModel
import ru.bagrusss.generator.model.Model
import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.*
import ru.bagrusss.generator.fields.EntityFactory
import java.io.InputStream
import java.io.PrintStream


class KotlinGenerator(input: InputStream,
                      output: PrintStream,
                      realmPath: String,
                      realmPackage: String,
                      prefix: String,
                      factory: EntityFactory): DefaultRealmGenerator(input, output, realmPath, realmPackage, prefix, factory) {

    override fun filter(node: DescriptorProtos.DescriptorProto): Boolean {
        return !protoFileJavaPackage.contains("google", true)
                && !node.name.contains("Swift", true)
    }

    override fun generatePrimitives(responseBuilder: PluginProtos.CodeGeneratorResponse.Builder) {
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
            responseBuilder.addFile(realmTypeFile)
        }
    }
}