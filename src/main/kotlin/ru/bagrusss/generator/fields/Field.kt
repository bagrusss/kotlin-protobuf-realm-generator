package ru.bagrusss.generator.fields

/**
 * Created by bagrusss on 12.07.17
 */
abstract class Field<T>(builder: FieldBuilder<T>) {
    protected val optional             = builder.optional
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
    protected val parentName           = builder.parentName
    protected val realmPackage         = builder.realmPackage
    protected val protoPackage         = builder.protoPackage

    protected val protoConstructorParameter = "protoModel"

    protected abstract fun getFieldType(): String
    protected abstract fun isPrimitive(): Boolean

    lateinit var toProtoInitializer: String
    lateinit var fromProtoInitializer: String
}