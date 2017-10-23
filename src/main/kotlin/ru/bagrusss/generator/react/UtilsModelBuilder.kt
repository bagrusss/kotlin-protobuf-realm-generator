package ru.bagrusss.generator.react

import java.util.*

abstract class UtilsModelBuilder<T> {

    internal var packageName = ""
    internal var fileName = ""

    internal val toWritableMapFunctions = LinkedList<FunModel<T>>()
    internal val fromReadableMapFunctions = LinkedList<FunModel<T>>()

    fun packageName (packageName: String) = apply {
        this.packageName = packageName
    }

    fun fileName (fileName: String) = apply {
        this.fileName = fileName
    }

    fun addFromReadableMapFun(func: FunModel<T>) = apply {
        fromReadableMapFunctions.add(func)
    }

    fun addToWritableMapFun(func: FunModel<T>) = apply {
        toWritableMapFunctions.add(func)
    }

    abstract fun build(): UtilsModel<T>

}