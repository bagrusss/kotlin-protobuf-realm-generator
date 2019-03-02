package ru.bagrusss.generator.realm

import com.squareup.kotlinpoet.ClassName
import ru.bagrusss.generator.model.Model
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder

abstract class RealmModel : Model {

    protected val className: ClassName

    constructor(builder: RealmModelBuilder) {
        className = builder.run { ClassName(realmPackageName, realmClassName) }
    }

    constructor(packageName: String, classNameStr: String) {
        className = ClassName(packageName, classNameStr)
    }

    abstract val fileExtension: String

}