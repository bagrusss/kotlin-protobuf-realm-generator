package ru.bagrusss.generator.model

import com.squareup.kotlinpoet.ClassName

abstract class Model {

    protected val className: ClassName

    constructor(builder: ModelBuilder) {
        className = ClassName(builder.realmPackageName, builder.realmClassName)
    }

    constructor(packageName: String, className: String) {
        this.className = ClassName(packageName, className)
    }

    constructor(clazz: ClassName) {
        className = clazz
    }

    abstract fun getFileExtension(): String
    abstract fun getFileName(): String
    abstract fun getModelBody(): String
}