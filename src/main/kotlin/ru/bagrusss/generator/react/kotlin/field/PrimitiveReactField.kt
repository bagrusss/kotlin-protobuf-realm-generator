package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.Utils


abstract class PrimitiveReactField<T: PrimitiveReactField<T>>(builder: ReactFieldBuilder<T>): ReactField<T>(builder) {

    private val checkOptional = "if (${Utils.getHas(fieldName)}()) "

    protected fun putToArrayMethodName() = "push" + getReactType()
    protected fun getFromArrayMethodName() = "get" + getReactType()

    protected open fun getPrimitiveInitializer() = ""
    protected open fun putPrimitiveInitializer() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ")"

    override fun putInitializer(): String {
        return if (optional) checkOptional + putPrimitiveInitializer() else putPrimitiveInitializer()
    }

    override fun getInitializer() = "map.get" + getReactType() + "(\"" + fieldName + "\")"

}