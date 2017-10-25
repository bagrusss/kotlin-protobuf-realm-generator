package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.Utils


class MessageReactField private constructor(builder: Builder): ReactField<MessageReactField>(builder) {

    override fun isPrimitive() = false

    override fun getReactType() = "Map"

    override fun toMapInit() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ".toWritableMap())"

    override fun fromMapInit() = "$protoFullTypeName.newBuilder().fromReadableMap(map.getMap(\"$fieldName\"))"

    override fun toMapConverter() = "it.${Utils.toMapMethod}()"

    class Builder: ReactFieldBuilder<MessageReactField>() {
        override fun build() = MessageReactField(this)
    }

}