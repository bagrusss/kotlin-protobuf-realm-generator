package ru.bagrusss.generator.generator

import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.fields.Field
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

    protected fun writeFile(path: String, fileName: String, classBody: String) {
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
        Logger.log("generate start size = ${request.protoFileList.size}")
        request.protoFileList.forEach { protoFile ->
            protoFilePackage = protoFile.`package`
            protoFileJavaPackage = protoFile.options.javaPackage
            packagesSet.add(protoFilePackage)
            protoToJavaPackagesMap.put(protoFilePackage, protoFileJavaPackage)


            Logger.log("proto package java ${protoFile.options.javaPackage}")
            protoFile.messageTypeList.forEach {
                handleProtoMessage(it)
            }
        }

        Logger.log("end")

        response.build()
                .writeTo(output)
    }

    abstract fun filter(node: DescriptorProtos.DescriptorProto): Boolean

    abstract fun handleProtoMessage(message:  DescriptorProtos.DescriptorProto)

    abstract fun generateProperty(field: DescriptorProtos.FieldDescriptorProto): Field<*>

}