package ru.bagrusss.generator.realm

import ru.bagrusss.generator.fields.Type
import ru.bagrusss.generator.realm.kotlin.fields.RealmFieldBuilder
import ru.bagrusss.generator.generator.Serializer
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder

abstract class RealmEntityFactory(serializer: Serializer) {

    abstract fun createBuilder(type: Type): RealmFieldBuilder<*>

    abstract fun createModelBuilder(): RealmModelBuilder
}