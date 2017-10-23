package ru.bagrusss.generator.generator

import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import ru.bagrusss.generator.Logger
import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.io.PrintWriter
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

    protected lateinit var response: PluginProtos.CodeGeneratorResponse.Builder
    protected lateinit var request:  PluginProtos.CodeGeneratorRequest

    protected fun writeClass(path: String, fileName: String, classBody: String) {
        val protoPackageDir = File(path)
        if (!protoPackageDir.exists())
            protoPackageDir.mkdir()

        val file = File(path, fileName)
        file.createNewFile()
        PrintWriter(file).use {
            it.write(classBody)
        }
    }

    open fun generate() {
        request.protoFileList.forEach { protoFile ->
            protoFilePackage = protoFile.`package`
            protoFileJavaPackage = protoFile.options.javaPackage
            packagesSet.add(protoFilePackage)
            protoToJavaPackagesMap.put(protoFilePackage, protoFileJavaPackage)


            Logger.log("proto package java ${protoFile.options.javaPackage}")
            protoFile.messageTypeList.forEach {
                handleProtoFile(it)
            }
        }

        response.build()
                .writeTo(output)
    }

    abstract fun filter(node: DescriptorProtos.DescriptorProto): Boolean

    open fun handleProtoFile(file:  DescriptorProtos.DescriptorProto) {

    }

}