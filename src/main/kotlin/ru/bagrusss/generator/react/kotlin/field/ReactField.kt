package ru.bagrusss.generator.react.kotlin.field

abstract class ReactField<T>(builder: ReactFieldBuilder<T>) {
    abstract fun putMethodName(): String
    abstract fun getMethodName(): String
}