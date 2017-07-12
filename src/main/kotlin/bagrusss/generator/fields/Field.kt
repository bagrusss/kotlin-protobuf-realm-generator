package bagrusss.generator.fields

import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 */
abstract class Field<T>(builder: FieldBuilder<T>) {
    protected val nullable             = builder.optional
    protected val repeated             = builder.repeated
    protected val typePrefix           = builder.typePrefix
    protected val typeSuffix           = builder.typeSuffix
    protected val protoFullTypeName    = builder.fullProtoTypeName
    protected val protoTypeName        = builder.protoTypeName
    protected val fieldName            = builder.fieldName
    protected val initializerFormat    = builder.initializerFormat
    protected val initializerArgs      = builder.initializerArgs
    protected val generateToProto      = builder.generateToProto
    protected val generateFromProto    = builder.generateFromProto
    protected val primaryKey           = builder.primaryKey
    protected val protoConstructorParameter = "protoModel"

    protected val propertySpec by lazy { getPropSpec() }
    protected val kotlinFieldType by lazy { getFieldType() }

    protected abstract fun getPropSpec(): PropertySpec
    protected abstract fun getFieldType(): String

    lateinit var toProtoInitializer: String
    lateinit var fromProtoInitializer: String

    protected fun realmListsInitialize(classTypeName: String?, toProtoBuilder: StringBuilder, realmProtoConstructorBuilder: StringBuilder, isPrimitive: Boolean) {
        toProtoBuilder.append("addAll")
                .append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1))
                .append('(')
                .append(fieldName)

        if (isPrimitive)
                toProtoBuilder.append(".map { it.value })\n")
        else toProtoBuilder.append(".map { it.toProto() })\n")

        realmProtoConstructorBuilder.append(".")
                .append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1))
                .append("Count > 0) {\n\t")
                .append(fieldName)
                .append(" = RealmList()")
                .append(fieldName)
                .append(".addAll(")
                .append(protoConstructorParameter)
                .append('.')
                .append(fieldName)
                .append("List.map { ")
                .append(typePrefix + classTypeName)
                .append("(it) })\n}")
    }
}