package ru.bagrusss.generator.generator

import com.google.protobuf.DescriptorProtos
import java.io.InputStream
import java.io.PrintStream
import java.util.TreeSet

abstract class Generator(protected val input: InputStream,
                         protected val output: PrintStream) {

    protected var protoFilePackage = ""
    protected var protoFileJavaPackage = ""

    protected val packagesSet = TreeSet<String>()
    protected val protoToJavaPackagesMap = HashMap<String, String>()
    protected val mapsSet = TreeSet<String>()

    @JvmField protected val OPTIONAL = DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL
    @JvmField protected val REPEATED = DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED

    abstract fun generate()

    abstract fun filter(node: DescriptorProtos.DescriptorProto): Boolean

}