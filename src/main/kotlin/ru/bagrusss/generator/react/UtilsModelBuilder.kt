package ru.bagrusss.generator.react

import java.util.*

abstract class UtilsModelBuilder<T> {

    internal var packageName = ""
    internal var fileName = ""

    internal val functions = LinkedList<FunModel<T>>()

    fun packageName (packageName: String) = apply {
        this.packageName = packageName
    }

    fun fileName (fileName: String) = apply {
        this.fileName = fileName
    }

    fun addFun(func: FunModel<T>) = apply {
        functions.add(func)
    }

    abstract fun build(): UtilsModel<T>

}