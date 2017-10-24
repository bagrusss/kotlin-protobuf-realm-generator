package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.Utils


abstract class ReactPrimitiveField<T: ReactPrimitiveField<T>>(builder: ReactFieldBuilder<T>): ReactField<T>(builder) {

    private val checkOptional = "if (${Utils.getHas(fieldName)}()) "

    protected fun putToArrayMethodName() = "push" + getReactType()
    protected fun getFromArrayMethodName() = "get" + getReactType()

    protected open fun getPrimitiveInitializer() = ""
    protected open fun putPrimitiveInitializer() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ")"

    override fun putInitializer(): String {
        return if (optional) checkOptional + putPrimitiveInitializer() else putPrimitiveInitializer()
    }

    override fun getInitializer() = "get" + getReactType() + "(\"" + fieldName + "\")"

}