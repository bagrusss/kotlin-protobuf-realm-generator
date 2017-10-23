package ru.bagrusss.generator.realm.kotlin.model

import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder
import java.util.*

abstract class KotlinRealmModelBuilder: RealmModelBuilder() {

    val fieldsList: LinkedList<Field<*>> = LinkedList()

    override fun <T> addField(field: Field<T>) = apply {
        fieldsList.add(field)
    }
}