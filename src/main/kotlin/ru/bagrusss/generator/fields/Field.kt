package ru.bagrusss.generator.fields

/**
 * Created by bagrusss on 12.07.17
 */
abstract class Field<T>(builder: FieldBuilder<T>) {
    @JvmField val optional             = builder.optional
    @JvmField val repeated             = builder.repeated
    @JvmField val typePrefix           = builder.typePrefix
    @JvmField val typeSuffix           = builder.typeSuffix
    @JvmField val protoFullTypeName    = builder.fullProtoTypeName
    @JvmField val fieldName            = builder.fieldName
    @JvmField val initializerFormat    = builder.initializerFormat
    @JvmField val initializerArgs      = builder.initializerArgs
    @JvmField val protoPackage         = builder.protoPackage

    protected abstract val isPrimitive: Boolean
}

enum class Type {
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