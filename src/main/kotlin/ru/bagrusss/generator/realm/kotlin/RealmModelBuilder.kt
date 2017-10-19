package ru.bagrusss.generator.realm.kotlin

import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.model.Model

abstract class RealmModelBuilder {

    internal var realmPackageName = ""
    internal var realmClassName = ""
    internal var protoClassFullName = ""

    fun realmPackageName(realmPackageName: String) = apply {
        this.realmPackageName = realmPackageName
    }

    fun realmClassName(realmClassName: String) = apply {
        this.realmClassName = realmClassName
    }

    fun protoClassFullName(protoClassFullName: String) = apply {
        this.protoClassFullName = protoClassFullName
    }

    abstract fun <T> addField(field: Field<T>)

    abstract fun isMap(isMap: Boolean): RealmModelBuilder

    abstract fun build(): Model

}