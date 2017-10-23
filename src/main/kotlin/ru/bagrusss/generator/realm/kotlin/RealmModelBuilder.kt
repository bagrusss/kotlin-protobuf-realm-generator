package ru.bagrusss.generator.realm.kotlin

import ru.bagrusss.generator.model.ModelBuilder

abstract class RealmModelBuilder: ModelBuilder() {

    internal var realmPackageName = ""
    internal var realmClassName = ""

    fun realmPackageName(realmPackageName: String) = apply {
        this.realmPackageName = realmPackageName
    }

    fun realmClassName(realmClassName: String) = apply {
        this.realmClassName = realmClassName
    }


}