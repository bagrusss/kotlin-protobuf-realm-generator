package ru.bagrusss.generator.realm

import google.protobuf.DescriptorProtos
import google.protobuf.compiler.PluginProtos
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.Type
import ru.bagrusss.generator.generator.Generator
import ru.bagrusss.generator.generator.ProtobufType
import ru.bagrusss.generator.realm.params.RealmParams
import java.io.File


abstract class DefaultRealmGenerator(params: RealmParams,
                                     private val entitiesFactory: RealmEntityFactory): Generator<RealmParams>(params) {

    protected inline val prefix
        get() = params.prefix

    private var count = 0

    abstract fun generatePrimitives(responseBuilder: PluginProtos.CodeGeneratorResponse.Builder)

    protected abstract fun isPrimaryKey(field: DescriptorProtos.FieldDescriptorProto): Boolean
    protected abstract fun isIndex(field: DescriptorProtos.FieldDescriptorProto): Boolean
    protected abstract fun additionalClass(node: DescriptorProtos.DescriptorProto): String
    protected abstract fun getLinkedObjects(node: DescriptorProtos.DescriptorProto): List<LinkedObject>

    override fun generate() {
        generatePrimitives(response)
        super.generate()
        Logger.log("realm generated $count models")
    }

    override fun handleProtoMessage(message: DescriptorProtos.DescriptorProto) {
        if (filter(message)) {
            parseCurrent(message)
            ++count
        }
    }

    private fun parseCurrent(node: DescriptorProtos.DescriptorProto, parentNameOriginal: String = "", parentNameRealm: String = "") {
        val currentRealmPackage = "$targetPackage.$protoFilePackage"
        val realmClassName = "${if (parentNameRealm.isNotEmpty()) parentNameRealm.replace(".", "") else prefix}${node.name}"
        val fullName = "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else ""}${node.name}"
        val protoFullName = "$protoFileJavaPackage.$fullName"

        node.nestedTypeList.forEach {
            val parentName = "${if (parentNameOriginal.isNotEmpty()) "$parentNameOriginal." else "" }${node.name}"
            parseCurrent(it, parentName, realmClassName)
        }

        if (node.fieldList.isNotEmpty()) {
            val isMap = node.options.mapEntry
            val classModelBuilder = entitiesFactory.newModelBuilder()
                                                   .apply {
                                                       realmPackageName(currentRealmPackage)
                                                       realmClassName(realmClassName)
                                                       protoClassFullName(protoFullName)
                                                       isMap(isMap)
                                                   }

            if (isMap)
                mapsSet.add("$protoFilePackage.$fullName")

            Logger.log("generate $protoFullName, nodeName = ${node.name}")

            node.fieldList.forEach { field ->
                classModelBuilder.addField(generateProperty(field))
            }

            getLinkedObjects(node).forEach {
                val clearName = it.fromType.replace(it.packageName, "")
                                           .replace(".", "")

                val linkedObject = entitiesFactory.newLinkedObjectsBuilder()
                                                  .propertyName(it.propertyName)
                                                  .fieldName(it.fieldName)
                                                  .repeated(true)
                                                  .fullProtoTypeName("$targetPackage.${it.packageName}.$prefix$clearName")
                                                  .build()

                classModelBuilder.addLinkedObject(linkedObject)
                Logger.log("linkedObjects_gen: $currentRealmPackage.${it.packageName}.$prefix$clearName, $it")
            }

            val model = classModelBuilder.build()

            writeFile("$targetPath${File.separator}$protoFilePackage", model.fileName, model.body)

            if (additionalClass(node).isNotEmpty()) {
                classModelBuilder.realmClassName("Additional$realmClassName")
                val additionalModel = classModelBuilder.build()
                writeFile("$targetPath${File.separator}$protoFilePackage", additionalModel.fileName, additionalModel.body)
            }
        }
    }

    override fun generateProperty(field: DescriptorProtos.FieldDescriptorProto): Field<*> {
        val fieldBuilder = when (field.type) {
            ProtobufType.TYPE_INT32,
            ProtobufType.TYPE_UINT32,
            ProtobufType.TYPE_FIXED32,
            ProtobufType.TYPE_SFIXED32  -> entitiesFactory.newBuilder(Type.INT)

            ProtobufType.TYPE_INT64,
            ProtobufType.TYPE_UINT64,
            ProtobufType.TYPE_FIXED64,
            ProtobufType.TYPE_SFIXED64  -> entitiesFactory.newBuilder(Type.LONG)

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
                               .apply { fullProtoTypeName("$javaPackage$clearTypeName") }
            }
            ProtobufType.TYPE_MESSAGE   -> {
                val protoTypeName = field.typeName.substring(1)
                val builder = if (!mapsSet.contains(protoTypeName))
                                  entitiesFactory.newBuilder(Type.MESSAGE)
                              else entitiesFactory.newBuilder(Type.MAP)

                val protoPackage = if (field.typeName.indexOf(protoFilePackage) == 1)
                                       protoFilePackage
                                   else packagesSet.first { field.typeName.indexOf(it) == 1 }

                val clearFullName = field.typeName
                                         .substring(protoPackage.length + 1)
                                         .replace(".", "")

                builder.apply {
                    fullProtoTypeName(clearFullName)
                    protoPackage("$protoPackage.")
                }
            }

            else                        -> throw UnsupportedOperationException("Field ${field.name} with type ${field.typeName} not supported")
        }

        val primaryKey = isPrimaryKey(field)
        val index = isIndex(field)

        fieldBuilder.realmPackage(targetPackage)
                    .primaryKey(primaryKey)
                    .indexed(index)
                    .optional(field.label == OPTIONAL || field.type == ProtobufType.TYPE_MESSAGE)
                    .repeated(field.label == REPEATED)
                    .fieldName(field.jsonName)
                    .prefix(prefix)

        return fieldBuilder.build()
    }

    class LinkedObject(
            @JvmField val fieldName: String,
            @JvmField val fromType: String,
            @JvmField val propertyName: String,
            @JvmField val packageName: String
    )

}