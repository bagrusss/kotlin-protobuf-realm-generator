package ru.bagrusss.generator.react.kotlin.field


class MessageReactField private constructor(builder: Builder): ReactField<MessageReactField>(builder) {

    override fun isPrimitive() = true // tempory

    override fun getReactType() = "Map"

    override fun putInitializer() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ".toWritableMap())"

    override fun getInitializer() = "$protoFullTypeName.newBuilder().fromReadableMap(map.getMap(\"$fieldName\"))"

    class Builder: ReactFieldBuilder<MessageReactField>() {
        override fun build() = MessageReactField(this)
    }

}