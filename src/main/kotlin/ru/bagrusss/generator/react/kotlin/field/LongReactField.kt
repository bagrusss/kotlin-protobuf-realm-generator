package ru.bagrusss.generator.react.kotlin.field

class LongReactField(builder: DoubleReactField.Builder): DoubleReactField(builder) {

    override fun getInitializer() = super.getInitializer() + ".toLong()"

    class Builder: DoubleReactField.Builder() {
        override fun build() = LongReactField(this)
    }

}