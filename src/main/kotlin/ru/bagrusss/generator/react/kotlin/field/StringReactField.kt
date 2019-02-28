package ru.bagrusss.generator.react.kotlin.field

open class StringReactField protected constructor(builder: Builder): ReactField<StringReactField>(builder) {

    override val isPrimitive = true

    override fun getReactTypeForMap() = "String"

    override fun getReactType() = getReactTypeForMap()

    open class Builder: ReactFieldBuilder<StringReactField>() {

        override fun build() = StringReactField(this)

    }
}