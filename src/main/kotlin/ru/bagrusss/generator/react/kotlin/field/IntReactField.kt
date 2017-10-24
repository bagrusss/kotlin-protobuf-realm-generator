package ru.bagrusss.generator.react.kotlin.field


class IntReactField private constructor(builder: Builder): PrimitiveReactField<IntReactField>(builder) {

    override fun getReactType() = "Int"

    override fun isPrimitive() = true

    class Builder: ReactFieldBuilder<IntReactField>() {
        override fun build() = IntReactField(this)

    }
}