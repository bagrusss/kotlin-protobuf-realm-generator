package ru.bagrusss.generator.realm

import ru.bagrusss.generator.realm.kotlin.fields.RealmFieldBuilder
import ru.bagrusss.generator.generator.Serializer
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder

abstract class RealmEntityFactory(serializer: Serializer) {

    abstract fun createBuilder(type: TYPE): RealmFieldBuilder<*>

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