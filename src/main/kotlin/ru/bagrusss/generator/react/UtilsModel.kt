package ru.bagrusss.generator.react

abstract class UtilsModel<T>(builder: UtilsModelBuilder<T>) {
    protected var packageName = ""
    protected var fileName = ""

    abstract fun getBody(): String
}