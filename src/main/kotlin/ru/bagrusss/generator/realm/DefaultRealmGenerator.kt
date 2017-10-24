package ru.bagrusss.generator.realm

import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.TYPE
import ru.bagrusss.generator.generator.Generator
import ru.bagrusss.generator.realm.kotlin.fields.RealmFieldBuilder
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder
import java.io.File
import java.io.InputStream
import java.io.PrintStream

internal typealias ProtobufType = DescriptorProtos.FieldDescriptorProto.Type

abstract class DefaultRealmGenerator(input: InputStream,
                                     output: PrintStream,
                                     private val realmPath: String,
                                     protected val realmPackage: String,
                                     protected val prefix: String,
                                     private val entitiesFactory: RealmEntityFactory): Generator(input, output) {


    abstract fun generatePrimitives(responseBuilder: PluginProtos.CodeGeneratorResponse.Builder)

    override fun generate() {
        response = PluginProtos.CodeGeneratorResponse.newBuilder()
        request = PluginProtos.CodeGeneratorRequest.parseFrom(input)

        generatePrimitives(response)

        super.generate()

        Logger.log("realm generated $count")
    }

    override fun handleProtoMessage(message: DescriptorProtos.DescriptorProto) {
        if (message.hasOptions() /*&& it.options.hasExtension(SwiftDescriptor.swiftMessageOptions)*/) {
            //if (it.hasOptions() && it.options.hasField(SwiftDescriptor.SwiftFileOptions.getDescriptor().fields.first { it.jsonName.contains("generate_realm_object", true) })) {
            parseCurrent(message)
            ++count
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
        Logger.log("Field_ name=${field.name}, type=${field.typeName}, field=$field")
        val fieldBuilder = when (field.type) {
            ProtobufType.TYPE_INT32     -> entitiesFactory.createBuilder(TYPE.INT)
            ProtobufType.TYPE_INT64     -> entitiesFactory.createBuilder(TYPE.LONG)
            ProtobufType.TYPE_FLOAT     -> entitiesFactory.createBuilder(TYPE.FLOAT)
            ProtobufType.TYPE_DOUBLE    -> entitiesFactory.createBuilder(TYPE.DOUBLE)
            ProtobufType.TYPE_STRING    -> entitiesFactory.createBuilder(TYPE.STRING)
            ProtobufType.TYPE_BOOL      -> entitiesFactory.createBuilder(TYPE.BOOL)
            ProtobufType.TYPE_BYTES     -> entitiesFactory.createBuilder(TYPE.BYTES)
            ProtobufType.TYPE_ENUM      -> {
                val protoPackage = packagesSet.first { field.typeName.indexOf(it) == 1 }
                val clearTypeName =  field.typeName
                                          .substring(1)
                                          .replace(protoPackage, "")

                val javaPackage = protoToJavaPackagesMap[protoPackage]
                entitiesFactory.createBuilder(TYPE.ENUM)
                                    .fullProtoTypeName("$javaPackage$clearTypeName") as RealmFieldBuilder<*>
            }
            ProtobufType.TYPE_MESSAGE   -> {
                val fullProtoName = field.typeName.substring(1)
                val builder = if (!mapsSet.contains(fullProtoName))
                                  entitiesFactory.createBuilder(TYPE.MESSAGE)
                              else entitiesFactory.createBuilder(TYPE.MAP)

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