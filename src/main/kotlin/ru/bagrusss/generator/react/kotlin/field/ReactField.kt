package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.fields.Field


abstract class ReactField<T: ReactField<T>>(builder: ReactFieldBuilder<T>): Field<T> (builder) {

    abstract fun getReactType(): String

    fun putMethodName() = "put" + getReactType()
    fun getMethodName() = "get" + getReactType()

    fun putToArrayMethodName() = "push" + getReactType()
    fun getFromArrayMethodName() = getMethodName()
}