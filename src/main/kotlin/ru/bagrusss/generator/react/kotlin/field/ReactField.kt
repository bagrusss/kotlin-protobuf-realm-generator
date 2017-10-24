package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.fields.Field


abstract class ReactField<T: ReactField<T>>(builder: ReactFieldBuilder<T>): Field<T> (builder) {

    abstract fun getReactType(): String
    open fun needSkip() = false

    abstract fun putInitializer(): String
    abstract fun getInitializer(): String
}