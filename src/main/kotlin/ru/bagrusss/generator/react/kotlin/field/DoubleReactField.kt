package ru.bagrusss.generator.react.kotlin.field

class DoubleReactField(builder: Builder): ReactField<DoubleReactField>(builder) {

    override fun getReactType() = "Double"

    override fun isPrimitive() = true

    class Builder: ReactFieldBuilder<DoubleReactField>() {

        override fun build() = DoubleReactField(this)

    }
}