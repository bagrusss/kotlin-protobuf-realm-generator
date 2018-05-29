package ru.bagrusss.generator.react.kotlin.field

class LongReactField private constructor(builder: StringReactField.Builder): StringReactField(builder) {

    override fun toMapInit(): String {
        val reactType = getReactType()
        return "put$reactType(\"$fieldName\", $fieldName.to$reactType())"
    }

    override fun fromMapInit() =  "$fieldName = map.get${getReactType()}(\"$fieldName\").toLong()"

    class Builder: StringReactField.Builder() {
        override fun build() = LongReactField(this)
    }

}