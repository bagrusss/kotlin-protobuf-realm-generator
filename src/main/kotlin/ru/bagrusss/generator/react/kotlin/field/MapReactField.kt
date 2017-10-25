package ru.bagrusss.generator.react.kotlin.field

class MapReactField private constructor(builder: Builder): MessageReactField(builder) {


    class Builder: MessageReactField.Builder() {
        override fun build() = MapReactField(this)
    }
}