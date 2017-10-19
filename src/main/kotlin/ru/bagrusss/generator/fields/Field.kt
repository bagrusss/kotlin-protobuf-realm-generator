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
    protected val parentName           = builder.parentName
    protected val protoPackage         = builder.protoPackage

    protected val protoConstructorParameter = "protoModel"

    protected abstract fun isPrimitive(): Boolean

    lateinit var toProtoInitializer: String
    lateinit var fromProtoInitializer: String
}

enum class TYPE {
    BOOL,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    STRING,
    BYTES,
    ENUM,
    MESSAGE,
    MAP
}