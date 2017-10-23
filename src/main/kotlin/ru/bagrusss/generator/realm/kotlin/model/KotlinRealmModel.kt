package ru.bagrusss.generator.realm.kotlin.model

import ru.bagrusss.generator.realm.RealmModel
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder

abstract class KotlinRealmModel<I> : RealmModel<I> {

    protected constructor(builder: RealmModelBuilder<I>) : super(builder)

    constructor(packageName: String, className: String) : super(packageName, className)

    override fun getFileExtension() = ".kt"

    override fun getFileName() = className.simpleName() + ".kt"

}