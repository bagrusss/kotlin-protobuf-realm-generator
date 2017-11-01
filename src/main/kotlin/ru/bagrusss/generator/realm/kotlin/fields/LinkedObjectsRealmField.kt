package ru.bagrusss.generator.realm.kotlin.fields

class LinkedObjectsRealmField private constructor(builder: Builder): KotlinRealmField<LinkedObjectsRealmField>(builder) {

    override fun isPrimitive() = false

    override fun getFieldType() = protoFullTypeName

    class Builder internal constructor(): RealmFieldBuilder<LinkedObjectsRealmField>() {

        override fun build() = LinkedObjectsRealmField(this)

    }
}