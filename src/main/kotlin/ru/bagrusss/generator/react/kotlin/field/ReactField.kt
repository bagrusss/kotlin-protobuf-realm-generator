package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.Utils
import ru.bagrusss.generator.fields.Field


abstract class ReactField<T: ReactField<T>>(builder: ReactFieldBuilder<T>): Field<T>(builder) {

    private val checkOptional = "if (${Utils.getHas(fieldName)}())\n\t\t"

    abstract fun getReactType(): String
    open fun needSkip() = false

    protected fun putToArrayMethodName() = "push" + getReactType()
    protected fun getFromArrayMethodName() = "get" + getReactType()

    protected open fun fromMapInit() = "map.get" + getReactType() + "(\"" + fieldName + "\")"
    protected open fun toMapInit() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ")"

    fun putInitializer(): String {
        return if (optional) checkOptional + toMapInit() else toMapInit()
    }

    fun getInitializer() = fromMapInit()

}