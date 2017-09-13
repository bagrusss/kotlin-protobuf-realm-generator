package ru.bagrusss.generator.kotlin.model

import ru.bagrusss.generator.model.Model
import ru.bagrusss.generator.model.RealmModelBuilder
import com.squareup.kotlinpoet.ClassName

abstract class KotlinModel : Model {


    protected constructor(builder: RealmModelBuilder) : super(builder)

    constructor(packageName: String, className: String) : super(packageName, className)

    constructor(clazz: ClassName) : super(clazz)

    override fun getFileExtension() = ".kt"

    override fun getFileName() = className.simpleName() + ".kt"

}