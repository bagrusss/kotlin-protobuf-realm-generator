package ru.bagrusss.generator.fields

/**
 * Created by bagrusss on 12.07.17
 */
abstract class Field<T>(builder: FieldBuilder<T>) {
    val optional             = builder.optional
    val repeated             = builder.repeated
    val typePrefix           = builder.typePrefix
    val typeSuffix           = builder.typeSuffix
    val protoFullTypeName    = builder.fullProtoTypeName
    val fieldName            = builder.fieldName
    val initializerFormat    = builder.initializerFormat
    val initializerArgs      = builder.initializerArgs
    val protoPackage         = builder.protoPackage

    protected abstract fun isPrimitive(): Boolean
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