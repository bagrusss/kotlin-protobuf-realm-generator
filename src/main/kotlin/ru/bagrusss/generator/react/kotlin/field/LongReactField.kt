package ru.bagrusss.generator.react.kotlin.field

class LongReactField private constructor(builder: DoubleReactField.Builder): DoubleReactField(builder) {

    override fun toMapInit() = "put${getReactType()}(\"$fieldName\", $fieldName.toDouble())"

    override fun fromMapInit() =  "get${getReactType()}(\"$fieldName\", $fieldName.toLong())"

    class Builder: DoubleReactField.Builder() {
        override fun build() = LongReactField(this)
    }

}