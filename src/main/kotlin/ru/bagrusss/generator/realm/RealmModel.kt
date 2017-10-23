package ru.bagrusss.generator.realm

import com.squareup.kotlinpoet.ClassName
import ru.bagrusss.generator.model.Model
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder

abstract class RealmModel : Model {

    protected val className: ClassName

    constructor(builder: RealmModelBuilder) {
        className = ClassName(builder.realmPackageName, builder.realmClassName)
    }

    constructor(packageName: String, className: String) {
        this.className = ClassName(packageName, className)
    }

    abstract fun getFileExtension(): String
    abstract fun getFileName(): String
    abstract fun getModelBody(): String
}