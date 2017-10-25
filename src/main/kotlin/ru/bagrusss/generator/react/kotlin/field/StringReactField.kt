package ru.bagrusss.generator.react.kotlin.field

open class StringReactField protected constructor(builder: Builder): ReactField<StringReactField>(builder) {

    override fun getReactTypeForMap() = "String"

    override fun getReactType() = getReactTypeForMap()

    override fun isPrimitive() = true

    open class Builder: ReactFieldBuilder<StringReactField>() {

        override fun build() = StringReactField(this)

    }
}