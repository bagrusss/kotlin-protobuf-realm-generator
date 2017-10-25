package ru.bagrusss.generator.react.kotlin.field


class EnumReactField private constructor(builder: Builder): StringReactField(builder) {

    override fun isPrimitive() = false

    override fun fromMapInit() = protoFullTypeName + ".valueOf(" + "map.get" + getReactType() + "(\"" + fieldName + "\"))"

    override fun toMapInit() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ".name)"

    override fun toMapConverter() = "it.name"

    class Builder: StringReactField.Builder() {
        override fun build() = EnumReactField(this)
    }
}