package ru.bagrusss.generator.realm.kotlin.model

import ru.bagrusss.generator.model.Model
import com.squareup.kotlinpoet.ClassName
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder

abstract class KotlinModel : Model {

    protected constructor(builder: RealmModelBuilder) : super(builder)

    constructor(packageName: String, className: String) : super(packageName, className)

    constructor(clazz: ClassName) : super(clazz)

    override fun getFileExtension() = ".kt"

    override fun getFileName() = className.simpleName() + ".kt"

}