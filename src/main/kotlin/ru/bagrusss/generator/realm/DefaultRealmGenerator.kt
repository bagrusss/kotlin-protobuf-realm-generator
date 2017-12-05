package ru.bagrusss.generator.realm

import google.protobuf.DescriptorProtos
import google.protobuf.compiler.PluginProtos
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.Type
import ru.bagrusss.generator.generator.Generator
import ru.bagrusss.generator.generator.ProtobufType
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder
import ru.bagrusss.generator.realm.kotlin.fields.RealmFieldBuilder
import java.io.File
import java.io.InputStream
import java.io.PrintStream


abstract class DefaultRealmGenerator(input: InputStream,
                                     output: PrintStream,
                                     private val realmPath: String,
                                     protected val realmPackage: String,
                                     protected val prefix: String,
                                     private val entitiesFactory: RealmEntityFactory): Generator(input, output) {
    private var count = 0

    abstract fun generatePrimitives(responseBuilder: PluginProtos.CodeGeneratorResponse.Builder)

    override fun generate() {

        generatePrimitives(response)

        super.generate()

        Logger.log("realm generated $count")
    }

    override fun handleProtoMessage(message: DescriptorProtos.DescriptorProto) {
        if (filter(message)) {
            parseCurrent(message)
            ++count
        }
    }

    private fun parseCurrent(node: DescriptorProtos.DescriptorProto, parentNameOriginal: String = "", parentNameRealm: String = "") {
        val currentRealmPackage = "$realmPackage.$protoFilePackage"
        val realmClassName = "${if (parentNameRealm.isNotEmpty()) parentNameRealm.replace(".", "") else prefix}${node.name}"
        val fullName = "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else ""}${node.name}"
        val protoFullName = "$protoFileJavaPackage.$fullName"

        node.nestedTypeList.forEach {
            parseCurrent(it, "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else "" }${node.name}", realmClassName)
        }

        if (node.fieldList.isNotEmpty()) {
            val isMap = node.options.mapEntry
            val classModelBuilder = entitiesFactory.newModelBuilder()
                                                   .realmPackageName(currentRealmPackage)
                                                   .realmClassName(realmClassName)
                                                   .protoClassFullName(protoFullName)
                                                   .isMap(isMap)
            if (isMap)
                mapsSet.add("$protoFilePackage.$fullName")

            Logger.log("generate $protoFullName, nodeName = ${node.name}")

            node.fieldList.forEach { field ->
                val property = generateProperty(field)
                classModelBuilder.addField(property)
            }

            val linkedObjects = getLinkedObjects(node)
            linkedObjects.forEach {
                val clearName = it.fromType.replace(it.packageName, "")
                                           .replace(".", "")

                val linkedObject = entitiesFactory.newLinkedObjectsBuilder()
                                                  .propertyName(it.propertyName)
                                                  .fieldName(it.fieldName)
                                                  .repeated(true)
                                                  .fullProtoTypeName("$realmPackage.${it.packageName}.$prefix$clearName")
                                                  .build()

                classModelBuilder as RealmModelBuilder
                classModelBuilder.addLinkedObject(linkedObject)
                Logger.log("linkedObjects_gen: $currentRealmPackage.${it.packageName}.$prefix$clearName, $it")
            }

            val model = classModelBuilder.build() as RealmModel

            writeFile("$realmPath${File.separator}$protoFilePackage", model.getFileName(), model.getModelBody())

            if (additionalClass(node).isNotEmpty()) {
                classModelBuilder as RealmModelBuilder
                classModelBuilder.realmClassName("Additional" + realmClassName)

                val additionalModel = classModelBuilder.build() as RealmModel

                writeFile("$realmPath${File.separator}$protoFilePackage", additionalModel.getFileName(), additionalModel.getModelBody())
            }
        }
    }

    override fun generateProperty(field: DescriptorProtos.FieldDescriptorProto): Field<*> {
        val fieldBuilder = when (field.type) {
            ProtobufType.TYPE_INT32     -> entitiesFactory.newBuilder(Type.INT)
            ProtobufType.TYPE_INT64     -> entitiesFactory.newBuilder(Type.LONG)
            ProtobufType.TYPE_FLOAT     -> entitiesFactory.newBuilder(Type.FLOAT)
            ProtobufType.TYPE_DOUBLE    -> entitiesFactory.newBuilder(Type.DOUBLE)
            ProtobufType.TYPE_STRING    -> entitiesFactory.newBuilder(Type.STRING)
            ProtobufType.TYPE_BOOL      -> entitiesFactory.newBuilder(Type.BOOL)
            ProtobufType.TYPE_BYTES     -> entitiesFactory.newBuilder(Type.BYTES)
            ProtobufType.TYPE_ENUM      -> {
                val protoPackage = packagesSet.first { field.typeName.indexOf(it) == 1 }
                val clearTypeName =  field.typeName
                                          .substring(1)
                                          .replace(protoPackage, "")

                val javaPackage = protoToJavaPackagesMap[protoPackage]
                entitiesFactory.newBuilder(Type.ENUM)
                               .fullProtoTypeName("$javaPackage$clearTypeName") as RealmFieldBuilder<*>
            }
            ProtobufType.TYPE_MESSAGE   -> {
                val protoTypeName = field.typeName.substring(1)
                val builder = if (!mapsSet.contains(protoTypeName))
                                  entitiesFactory.newBuilder(Type.MESSAGE)
                              else entitiesFactory.newBuilder(Type.MAP)

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
        val primaryKey = isPrimaryKey(field)
        val index = isIndex(field)

        fieldBuilder.realmPackage(realmPackage)
                    .primaryKey(primaryKey)
                    .indexed(index)
                    .optional(field.label == OPTIONAL || field.type == ProtobufType.TYPE_MESSAGE)
                    .repeated(field.label == REPEATED)
                    .fieldName(field.name)
                    .prefix(prefix)

        return fieldBuilder.build()
    }

    protected abstract fun isPrimaryKey(field: DescriptorProtos.FieldDescriptorProto): Boolean

    protected abstract fun isIndex(field: DescriptorProtos.FieldDescriptorProto): Boolean

    protected abstract fun additionalClass(node: DescriptorProtos.DescriptorProto): String

    protected abstract fun getLinkedObjects(node: DescriptorProtos.DescriptorProto): List<LinkedObject>

    data class LinkedObject(
            val fieldName: String,
            val fromType: String,
            val propertyName: String,
            val packageName: String
    )

}