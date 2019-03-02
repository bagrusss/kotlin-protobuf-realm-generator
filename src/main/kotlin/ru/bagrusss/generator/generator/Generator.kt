package ru.bagrusss.generator.generator

import com.google.protobuf.ExtensionRegistryLite
import google.protobuf.DescriptorProtos
import google.protobuf.KotlinDescriptor
import google.protobuf.SwiftDescriptor
import google.protobuf.compiler.PluginProtos
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.fields.Field
import java.io.File
import java.io.PrintWriter
import java.util.TreeSet

internal typealias ProtobufType = DescriptorProtos.FieldDescriptorProto.Type

abstract class Generator<P: Params<P>>(@JvmField protected val params: P) {

    protected var protoFilePackage = ""
    protected var protoFileJavaPackage = ""

    protected val packagesSet = TreeSet<String>()
    protected val protoToJavaPackagesMap = HashMap<String, String>()
    protected val mapsSet = TreeSet<String>()

    @JvmField
    protected val extensionRegistry: ExtensionRegistryLite = ExtensionRegistryLite.newInstance()
    protected val response: PluginProtos.CodeGeneratorResponse.Builder = PluginProtos.CodeGeneratorResponse
                                                                                     .newBuilder()
    private val request: PluginProtos.CodeGeneratorRequest = PluginProtos.CodeGeneratorRequest
                                                                         .parseFrom(params.inputStream, extensionRegistry)

    protected inline val targetPath
        get() = params.targetPath
    protected inline val targetPackage
        get() = params.targetPackage


    protected fun writeFile(path: String, fileName: String, classBody: String) {
        val protoPackageDir = File(path)
        if (!protoPackageDir.exists())
            protoPackageDir.mkdir()

        File(path, fileName).run {
            createNewFile()
            PrintWriter(this).use { it.write(classBody) }
        }
    }

    open fun generate() {
        SwiftDescriptor.registerAllExtensions(extensionRegistry)
        KotlinDescriptor.registerAllExtensions(extensionRegistry)

        Logger.log("generate start size = ${request.protoFileList.size}")
        request.protoFileList.forEach { protoFile ->
            protoFilePackage = protoFile.`package`
            protoFileJavaPackage = protoFile.options.javaPackage
            packagesSet.add(protoFilePackage)
            protoToJavaPackagesMap[protoFilePackage] = protoFileJavaPackage

            Logger.log("proto package java ${protoFile.options.javaPackage}")
            protoFile.messageTypeList.forEach(::handleProtoMessage)
        }

        Logger.log("end")

        response.build()
                .writeTo(params.outputStream)
    }

    abstract fun filter(node: DescriptorProtos.DescriptorProto): Boolean
    abstract fun handleProtoMessage(message:  DescriptorProtos.DescriptorProto)
    abstract fun generateProperty(field: DescriptorProtos.FieldDescriptorProto): Field<*>

    companion object {
        @JvmField val OPTIONAL = DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL
        @JvmField val REPEATED = DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED
    }

}