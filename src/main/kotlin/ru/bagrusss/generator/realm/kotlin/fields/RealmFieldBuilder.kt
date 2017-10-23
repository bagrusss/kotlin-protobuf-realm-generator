package ru.bagrusss.generator.realm.kotlin.fields

import ru.bagrusss.generator.fields.FieldBuilder

abstract class RealmFieldBuilder<T>: FieldBuilder<T>() {

    internal var primaryKey     = false
    internal var realmPackage   = ""
    internal var generateToProto = true
    internal var generateFromProto = true

    fun generateToProto(generate: Boolean) = apply {
        this.generateToProto = generate
    }

    fun generateFromProto(generate: Boolean) = apply {
        this.generateFromProto = generate
    }

    fun primaryKey(primary: Boolean) = apply {
        this.primaryKey = primary
    }

    fun realmPackage(pkg: String) = apply {
        this.realmPackage = pkg
    }
}