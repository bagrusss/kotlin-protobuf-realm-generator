package ru.bagrusss.generator.realm

import com.google.protobuf.ExtensionRegistryLite
import google.protobuf.DescriptorProtos
import google.protobuf.KotlinDescriptor
import google.protobuf.SwiftDescriptor
import google.protobuf.compiler.PluginProtos
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.Type
import ru.bagrusss.generator.generator.Generator
import ru.bagrusss.generator.generator.ProtobufType
import ru.bagrusss.generator.realm.kotlin.fields.RealmFieldBuilder
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder
import java.io.File
import java.io.InputStream
import java.io.PrintStream


abstract class DefaultRealmGenerator(input: InputStream,
                                     output: PrintStream,
                                     private val realmPath: String,
                                     protected val realmPackage: String,
                                     protected val prefix: String,
                                     private val entitiesFactory: RealmEntityFactory): Generator(input, output) {


    abstract fun generatePrimitives(responseBuilder: PluginProtos.CodeGeneratorResponse.Builder)

    override fun generate() {

        val extensionRegistry = ExtensionRegistryLite.newInstance()
        SwiftDescriptor.registerAllExtensions(extensionRegistry)
        KotlinDescriptor.registerAllExtensions(extensionRegistry)

        response = PluginProtos.CodeGeneratorResponse.newBuilder()
        request = PluginProtos.CodeGeneratorRequest.parseFrom(input, extensionRegistry)

        generatePrimitives(response)

        super.generate()

        Logger.log("realm generated $count")
    }

    override fun handleProtoMessage(message: DescriptorProtos.DescriptorProto) {
        if (message.hasOptions()
                && (message.options.hasExtension(SwiftDescriptor.swiftMessageOptions)
                    || message.options.hasExtension(KotlinDescriptor.kotlinMessageOptions))) {
            val extension = message.options.getExtension(SwiftDescriptor.swiftMessageOptions)
            val kotlinExtension = message.options.getExtension(KotlinDescriptor.kotlinMessageOptions)
            if (extension.generateRealmObject || kotlinExtension?.generateRealmObject == true) {
                parseCurrent(message)
                ++count
            }
        }
    }

    private fun parseCurrent(node: DescriptorProtos.DescriptorProto, parentNameOriginal: String = "", parentNameRealm: String = "") {
        if (filter(node)) {
            val realmPackage = "$realmPackage.$protoFilePackage"
            val realmClassName = "${if (parentNameRealm.isNotEmpty()) parentNameRealm.replace(".", "") else prefix}${node.name}"
            val fullName = "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else ""}${node.name}"
            val protoFullName = "$protoFileJavaPackage.$fullName"

            node.nestedTypeList.forEach {
                parseCurrent(it, "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else "" }${node.name}", realmClassName)
            }

            if (node.fieldList.isNotEmpty()) {
                val isMap = node.options.mapEntry
                val classModelBuilder = entitiesFactory.createModelBuilder()
                                                       .realmPackageName(realmPackage)
                                                       .realmClassName(realmClassName)
                                                       .protoClassFullName(protoFullName)
                                                       .isMap(isMap) as RealmModelBuilder
                if (isMap)
                    mapsSet.add("$protoFilePackage.$fullName")

                Logger.log("generate $protoFullName, nodeName = ${node.name}")

                node.fieldList.forEach { field ->
                    val property = generateProperty(field)
                    classModelBuilder.addField(property)
                }

                val model = classModelBuilder.build() as RealmModel

                writeFile("$realmPath${File.separator}$protoFilePackage", model.getFileName(), model.getModelBody())

            }

        }

    }

    private var count = 0


    override fun generateProperty(field: DescriptorProtos.FieldDescriptorProto): Field<*> {
        val fieldBuilder = when (field.type) {
            ProtobufType.TYPE_INT32     -> entitiesFactory.createBuilder(Type.INT)
            ProtobufType.TYPE_INT64     -> entitiesFactory.createBuilder(Type.LONG)
            ProtobufType.TYPE_FLOAT     -> entitiesFactory.createBuilder(Type.FLOAT)
            ProtobufType.TYPE_DOUBLE    -> entitiesFactory.createBuilder(Type.DOUBLE)
            ProtobufType.TYPE_STRING    -> entitiesFactory.createBuilder(Type.STRING)
            ProtobufType.TYPE_BOOL      -> entitiesFactory.createBuilder(Type.BOOL)
            ProtobufType.TYPE_BYTES     -> entitiesFactory.createBuilder(Type.BYTES)
            ProtobufType.TYPE_ENUM      -> {
                val protoPackage = packagesSet.first { field.typeName.indexOf(it) == 1 }
                val clearTypeName =  field.typeName
                                          .substring(1)
                                          .replace(protoPackage, "")

                val javaPackage = protoToJavaPackagesMap[protoPackage]
                entitiesFactory.createBuilder(Type.ENUM)
                                    .fullProtoTypeName("$javaPackage$clearTypeName") as RealmFieldBuilder<*>
            }
            ProtobufType.TYPE_MESSAGE   -> {
                val protoTypeName = field.typeName.substring(1)
                val builder = if (!mapsSet.contains(protoTypeName))
                                  entitiesFactory.createBuilder(Type.MESSAGE)
                              else entitiesFactory.createBuilder(Type.MAP)

                val protoPackage = if (field.typeName.indexOf(protoFilePackage) == 1)
                                       protoFilePackage
                                   else packagesSet.first { field.typeName.indexOf(it) == 1 }

                val clearedFullName =  field.typeName
                                            .substring(protoPackage.length + 1)
                                            .replace(".", "")

                builder.fullProtoTypeName(clearedFullName)
                       .protoPackage("$protoPackage.") as RealmFieldBuilder<*>
            }

            else                        -> throw UnsupportedOperationException("name=${field.name}, type=${field.typeName}")
        }


        fieldBuilder.realmPackage(realmPackage)                                         //Just for maps
                    .primaryKey(field.hasOptions() /*|| field.name == "id"*/ || (field.name == "key" && field.type == ProtobufType.TYPE_STRING))
                    .optional(field.label == OPTIONAL)
                    .repeated(field.label == REPEATED)
                    .fieldName(field.name)
                    .prefix(prefix)

        return fieldBuilder.build()
    }

}