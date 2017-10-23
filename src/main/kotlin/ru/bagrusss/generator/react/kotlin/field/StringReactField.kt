package ru.bagrusss.generator.react.kotlin.field

class StringReactField(builder: Builder): ReactField<StringReactField>(builder) {

    override fun getReactType() = "String"

    override fun isPrimitive() = true

    class Builder: ReactFieldBuilder<StringReactField>() {

        override fun build() = StringReactField(this)

    }
}