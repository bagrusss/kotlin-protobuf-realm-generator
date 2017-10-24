package ru.bagrusss.generator.react.kotlin.field

class BytesReactField private constructor(builder: Builder): StringReactField(builder) {

    override fun needSkip() = true

    class Builder: StringReactField.Builder() {
        override fun build() = BytesReactField(this)
    }
}