package ru.bagrusss.generator.react.kotlin.field

class FloatReactField private constructor(builder: Builder): DoubleReactField(builder) {

    override fun getInitializer() = super.getInitializer() + ".toFloat()"

    class Builder: DoubleReactField.Builder() {
        override fun build() = FloatReactField(this)
    }
}