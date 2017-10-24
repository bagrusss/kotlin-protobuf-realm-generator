package ru.bagrusss.generator.react.kotlin.field

class FloatReactField private constructor(builder: Builder): DoubleReactField(builder) {

    override fun toMapInit() = "put${getReactType()}(\"$fieldName\", $fieldName.toDouble())"

    override fun fromMapInit() = "get${getReactType()}(\"$fieldName\", $fieldName.toFloat()"

    class Builder: DoubleReactField.Builder() {
        override fun build() = FloatReactField(this)
    }
}