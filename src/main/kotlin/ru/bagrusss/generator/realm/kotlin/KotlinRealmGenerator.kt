package ru.bagrusss.generator.realm.kotlin

import com.google.protobuf.ExtensionRegistryLite
import ru.bagrusss.generator.realm.kotlin.model.KotlinPrimitiveModel
import com.squareup.kotlinpoet.*
import google.protobuf.DescriptorProtos
import google.protobuf.KotlinDescriptor
import google.protobuf.SwiftDescriptor
import google.protobuf.compiler.PluginProtos
import ru.bagrusss.generator.realm.RealmEntityFactory
import ru.bagrusss.generator.realm.DefaultRealmGenerator
import java.io.InputStream
import java.io.PrintStream

class KotlinRealmGenerator(input: InputStream,
                           output: PrintStream,
                           realmPath: String,
                           realmPackage: String,
                           prefix: String,
                           factory: RealmEntityFactory): DefaultRealmGenerator(input, output, realmPath, realmPackage, prefix, factory) {

    override fun filter(node: DescriptorProtos.DescriptorProto): Boolean {
        return node.hasOptions()
                && node.options.hasExtension(SwiftDescriptor.swiftMessageOptions) && node.options.getExtension(SwiftDescriptor.swiftMessageOptions).generateRealmObject
                || node.options.hasExtension(KotlinDescriptor.kotlinMessageOptions) && node.options.getExtension(KotlinDescriptor.kotlinMessageOptions).generateRealmObject
                || node.options.mapEntry
    }

    override fun generatePrimitives(responseBuilder: PluginProtos.CodeGeneratorResponse.Builder) {
        listOf(Pair(INT, 0),
                Pair(LONG, 0L),
                Pair(FLOAT, "0f"),
                Pair(DOUBLE, 0.0),
                Pair(BOOLEAN, false),
                Pair(ClassName("kotlin", "String"), "\"\"")).forEach {
            val primitiveModel = KotlinPrimitiveModel(realmPackage, prefix, it.first, it.second)
            val realmTypeFile = PluginProtos.CodeGeneratorResponse
                                            .File
                                            .newBuilder()
                                            .setName(primitiveModel.fileName)
                                            .setContent(primitiveModel.body)
                                            .build()
            responseBuilder.addFile(realmTypeFile)
        }
    }

    override fun generate() {
        val extensionRegistry = ExtensionRegistryLite.newInstance()
        SwiftDescriptor.registerAllExtensions(extensionRegistry)
        KotlinDescriptor.registerAllExtensions(extensionRegistry)

        response = PluginProtos.CodeGeneratorResponse.newBuilder()
        request = PluginProtos.CodeGeneratorRequest.parseFrom(input, extensionRegistry)

        super.generate()
    }

    override fun additionalClass(node: DescriptorProtos.DescriptorProto): String {
        return if (node.hasOptions()
                    && node.options.hasExtension(SwiftDescriptor.swiftMessageOptions)
                    && node.options.getExtension(SwiftDescriptor.swiftMessageOptions).hasAdditionalClassName()) {
                    node.options.getExtension(SwiftDescriptor.swiftMessageOptions).additionalClassName
               } else ""
    }

    override fun isPrimaryKey(field: DescriptorProtos.FieldDescriptorProto): Boolean {
        return field.name == "id"
                || field.hasOptions()
                && field.options.hasExtension(SwiftDescriptor.swiftFieldOptions)
                && field.options.getExtension(SwiftDescriptor.swiftFieldOptions).realmPrimaryKey
                || field.options.hasExtension(KotlinDescriptor.kotlinFieldOptions)
                && field.options.getExtension(KotlinDescriptor.kotlinFieldOptions).generateRealmPrimaryKey
    }

    override fun isIndex(field: DescriptorProtos.FieldDescriptorProto): Boolean {
        return field.hasOptions()
                && field.options.hasExtension(SwiftDescriptor.swiftFieldOptions)
                && field.options.getExtension(SwiftDescriptor.swiftFieldOptions).realmIndexedPropertie
    }

    override fun getLinkedObjects(node: DescriptorProtos.DescriptorProto): List<LinkedObject> {
        return if (node.hasOptions() && node.options.hasExtension(SwiftDescriptor.swiftMessageOptions)) {
                   val extension = node.options.getExtension(SwiftDescriptor.swiftMessageOptions)
                   extension.linkedObjectsList.map {
                       LinkedObject(fieldName = it.fieldName,
                                    fromType = it.fromType,
                                    packageName = it.packageName,
                                    propertyName = it.propertyName)
                   }
               } else {
                   emptyList()
               }
    }
}