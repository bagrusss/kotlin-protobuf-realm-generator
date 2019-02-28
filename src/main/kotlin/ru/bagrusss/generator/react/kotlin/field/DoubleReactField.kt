package ru.bagrusss.generator.react.kotlin.field

open class DoubleReactField protected constructor(builder: Builder): ReactField<DoubleReactField>(builder) {

    override val isPrimitive = true
    override fun getReactTypeForMap() = "Double"
    override fun getReactType() = getReactTypeForMap()


    open class Builder: ReactFieldBuilder<DoubleReactField>() {

        override fun build() = DoubleReactField(this)

    }
}