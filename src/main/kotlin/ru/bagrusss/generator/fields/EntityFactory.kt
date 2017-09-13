package ru.bagrusss.generator.fields

import ru.bagrusss.generator.generator.Serializer
import ru.bagrusss.generator.model.RealmModelBuilder

abstract class EntityFactory(serializer: Serializer) {

    abstract fun createBuilder(type: TYPE): FieldBuilder<*>

    abstract fun createModelBuilder(): RealmModelBuilder
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