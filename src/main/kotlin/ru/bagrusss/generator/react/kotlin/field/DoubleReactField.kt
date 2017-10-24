package ru.bagrusss.generator.react.kotlin.field

open class DoubleReactField protected constructor(builder: Builder): PrimitiveReactField<DoubleReactField>(builder) {

    override fun getReactType() = "Double"

    override fun isPrimitive() = true

    open class Builder: ReactFieldBuilder<DoubleReactField>() {

        override fun build() = DoubleReactField(this)

    }
}