package ru.bagrusss.generator.generator

import com.google.protobuf.DescriptorProtos

abstract class Generator(serializer: Serializer) {

    abstract fun generate()

    abstract fun filter(node: DescriptorProtos.DescriptorProto): Boolean

}