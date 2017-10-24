package ru.bagrusss.generator.react.kotlin.field


class BoolReactField private constructor(builder: Builder): ReactPrimitiveField<BoolReactField>(builder) {

    override fun getReactType() = "Boolean"

    override fun isPrimitive() = true

    class Builder: ReactFieldBuilder<BoolReactField>() {

        override fun build() = BoolReactField(this)

    }

}