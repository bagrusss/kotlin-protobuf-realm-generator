package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.Utils


open class MessageReactField protected constructor(builder: Builder): ReactField<MessageReactField>(builder) {

    override fun isPrimitive() = false

    override fun getReactType() = "Map"

    override fun toMapInit() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ".toWritableMap())"

    override fun fromMapInit() = "$fieldName = $protoFullTypeName.newBuilder().fromReadableMap(map.getMap(\"$fieldName\"))"

    override fun toMapConverter() = "it.${Utils.toMapMethod}()"

    open class Builder: ReactFieldBuilder<MessageReactField>() {
        override fun build() = MessageReactField(this)
    }

}