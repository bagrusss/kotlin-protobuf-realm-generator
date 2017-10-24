package ru.bagrusss.generator.react.kotlin.field


class MessageReactField private constructor(builder: Builder): ReactField<MessageReactField>(builder) {

    override fun isPrimitive() = true // tempory

    override fun getReactType() = "Map"

    override fun toMapInit() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ".toWritableMap())"

    override fun fromMapInit() = "$protoFullTypeName.newBuilder().fromReadableMap(map.getMap(\"$fieldName\"))"

    class Builder: ReactFieldBuilder<MessageReactField>() {
        override fun build() = MessageReactField(this)
    }

}