package ru.bagrusss.generator.react.kotlin.field

class MapReactField private constructor(builder: Builder): MessageReactField(builder) {

    val keyType = builder.keyType
    val valueType = builder.valueType


    /* to Map
    val requisitesArray = com.facebook.react.bridge.Arguments.createArray()
    for ((k, v) in requisitesMap) {
        val item = com.facebook.react.bridge.Arguments.createMap()
        item.putString(k, v)
        requisitesArray.pushMap(item)
    }*/


    class Builder: MessageReactField.Builder() {
        internal var keyType = ""
        internal var valueType = ""

        fun keyType(keyType: String) = apply {
            this.keyType = keyType
        }

        fun valueType(valueType: String) = apply {
            this.valueType = valueType
        }

        override fun build() = MapReactField(this)
    }
}