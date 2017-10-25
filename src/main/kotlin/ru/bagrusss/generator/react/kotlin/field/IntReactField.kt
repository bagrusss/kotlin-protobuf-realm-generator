package ru.bagrusss.generator.react.kotlin.field


class IntReactField private constructor(builder: Builder): ReactField<IntReactField>(builder) {

    override fun getReactType() = getReactTypeForMap()

    override fun getReactTypeForMap() = "Int"

    override fun isPrimitive() = true

    class Builder: ReactFieldBuilder<IntReactField>() {
        override fun build() = IntReactField(this)

    }
}