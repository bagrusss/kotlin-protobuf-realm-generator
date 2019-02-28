package ru.bagrusss.generator.react.kotlin.field


class BoolReactField private constructor(builder: Builder): ReactField<BoolReactField>(builder) {

    override val isPrimitive = true

    override fun getReactTypeForMap() = "Boolean"

    override fun getReactType() = getReactTypeForMap()

    class Builder: ReactFieldBuilder<BoolReactField>() {

        override fun build() = BoolReactField(this)

    }

}