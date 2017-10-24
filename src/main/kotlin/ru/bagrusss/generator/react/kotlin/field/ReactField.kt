package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.Utils
import ru.bagrusss.generator.fields.Field


abstract class ReactField<T: ReactField<T>>(builder: ReactFieldBuilder<T>): Field<T>(builder) {

    private val checkOptional = "if (${Utils.getHas(fieldName)}()) "

    abstract fun getReactType(): String
    open fun needSkip() = false

    protected fun putToArrayMethodName() = "push" + getReactType()
    protected fun getFromArrayMethodName() = "get" + getReactType()

    protected open fun getPrimitiveInitializer() = ""
    protected open fun putPrimitiveInitializer() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ")"

    open fun putInitializer(): String {
        return if (optional) checkOptional + putPrimitiveInitializer() else putPrimitiveInitializer()
    }

    open fun getInitializer() = "map.get" + getReactType() + "(\"" + fieldName + "\")"

}