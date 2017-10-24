package ru.bagrusss.generator.react.kotlin.field

open class StringReactField(builder: Builder): PrimitiveReactField<StringReactField>(builder) {

    override fun getReactType() = "String"

    override fun isPrimitive() = true

    open class Builder: ReactFieldBuilder<StringReactField>() {

        override fun build() = StringReactField(this)

    }
}