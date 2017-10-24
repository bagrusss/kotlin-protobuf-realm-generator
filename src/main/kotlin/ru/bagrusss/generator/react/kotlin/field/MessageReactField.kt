package ru.bagrusss.generator.react.kotlin.field


class MessageReactField private constructor(builder: Builder): PrimitiveReactField<MessageReactField>(builder) {

    override fun isPrimitive() = true // tempory

    override fun getReactType() = "Map"

    override fun putInitializer() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ".toWritableMap())"

    override fun getInitializer() = "$protoFullTypeName.Builder.fromReadableMap(map.getMap(\"$fieldName\"))"

    class Builder: ReactFieldBuilder<MessageReactField>() {
        override fun build() = MessageReactField(this)
    }

}