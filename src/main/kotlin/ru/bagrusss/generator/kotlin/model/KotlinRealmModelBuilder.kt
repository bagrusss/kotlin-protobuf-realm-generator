package ru.bagrusss.generator.kotlin.model

import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.model.RealmModelBuilder
import java.util.*

abstract class KotlinRealmModelBuilder: RealmModelBuilder() {

    val fieldsList: LinkedList<Field<*>> = LinkedList()

    override fun <T> addField(field: Field<T>) {
        fieldsList.add(field)
    }
}