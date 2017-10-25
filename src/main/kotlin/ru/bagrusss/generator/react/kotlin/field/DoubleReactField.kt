package ru.bagrusss.generator.react.kotlin.field

open class DoubleReactField protected constructor(builder: Builder): ReactField<DoubleReactField>(builder) {

    override fun getReactType() = getReactTypeForMap()

    override fun getReactTypeForMap() = "Double"

    override fun isPrimitive() = true

    open class Builder: ReactFieldBuilder<DoubleReactField>() {

        override fun build() = DoubleReactField(this)

    }
}