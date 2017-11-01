package ru.bagrusss.generator.realm

import ru.bagrusss.generator.fields.Type
import ru.bagrusss.generator.realm.kotlin.fields.RealmFieldBuilder
import ru.bagrusss.generator.generator.Serializer
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder
import ru.bagrusss.generator.realm.kotlin.fields.LinkingObjectsRealmField

abstract class RealmEntityFactory(serializer: Serializer) {

    abstract fun newBuilder(type: Type): RealmFieldBuilder<*>

    abstract fun newModelBuilder(): RealmModelBuilder

    abstract fun newLinkedObjectsBuilder(): LinkingObjectsRealmField.Builder
}