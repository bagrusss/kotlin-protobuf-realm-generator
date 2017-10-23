package ru.bagrusss.generator.realm.kotlin

import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.model.ModelBuilder
import java.util.*

abstract class RealmModelBuilder<I>: ModelBuilder<I>() {

    internal var realmPackageName = ""
    internal var realmClassName = ""

    internal var isMap = false

    val fieldsList: LinkedList<Field<*>> = LinkedList()

    override fun <T> addField(field: Field<T>) = apply {
        fieldsList.add(field)
    }

    fun realmPackageName(realmPackageName: String) = apply {
        this.realmPackageName = realmPackageName
    }

    fun realmClassName(realmClassName: String) = apply {
        this.realmClassName = realmClassName
    }

    override fun isMap(isMap: Boolean) = apply {
        this.isMap = isMap
    }

}