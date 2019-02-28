package ru.bagrusss.generator.react.kotlin.field


class IntReactField private constructor(builder: Builder): ReactField<IntReactField>(builder) {

    override val isPrimitive = true
    override fun getReactTypeForMap() = "Int"
    override fun getReactType() = getReactTypeForMap()

    class Builder: ReactFieldBuilder<IntReactField>() {
        override fun build() = IntReactField(this)

    }
}