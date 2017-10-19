package ru.bagrusss.generator.react.kotlin

import com.google.protobuf.DescriptorProtos
import ru.bagrusss.generator.generator.Generator
import java.io.InputStream
import java.io.PrintStream

class KotlinReactGenerator(input: InputStream,
                           output: PrintStream): Generator(input, output) {

    override fun generate() {

    }

    override fun filter(node: DescriptorProtos.DescriptorProto): Boolean {
        return !protoFileJavaPackage.contains("google", true)
                && !node.name.contains("Swift", true)
    }

}