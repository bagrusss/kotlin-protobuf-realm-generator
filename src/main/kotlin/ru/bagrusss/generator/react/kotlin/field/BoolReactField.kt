package ru.bagrusss.generator.react.kotlin.field


class BoolReactField private constructor(builder: Builder): ReactField<BoolReactField>(builder) {

    override fun getReactTypeForMap() = "Boolean"

    override fun getReactType() = getReactTypeForMap()

    override fun isPrimitive() = true

    class Builder: ReactFieldBuilder<BoolReactField>() {

        override fun build() = BoolReactField(this)

    }

}