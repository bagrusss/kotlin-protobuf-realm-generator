package ru.bagrusss.generator.react.kotlin.field


class EnumReactField private constructor(builder: Builder): StringReactField(builder) {

    override val isPrimitive = false

    override fun fromMapInit() = fieldName + " = " + protoFullTypeName + ".valueOf(" + "map.get" + getReactType() + "(\"" + fieldName + "\"))"

    override fun toMapInit() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ".name)"

    override fun toMapConverter() = "it.name"

    override fun getFromMapInit() = "$protoFullTypeName.valueOf("


    class Builder: StringReactField.Builder() {
        override fun build() = EnumReactField(this)
    }
}