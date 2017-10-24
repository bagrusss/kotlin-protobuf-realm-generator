package ru.bagrusss.generator.react.kotlin.field

class EnumReactField(builder: Builder): StringReactField(builder) {

    override fun getInitializer() =  protoFullTypeName + ".valueOf(" + "map.get" + getReactType() + "(\"" + fieldName + "\"))"

    override fun putInitializer() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ".name)"

    class Builder: StringReactField.Builder() {
        override fun build() = EnumReactField(this)
    }
}