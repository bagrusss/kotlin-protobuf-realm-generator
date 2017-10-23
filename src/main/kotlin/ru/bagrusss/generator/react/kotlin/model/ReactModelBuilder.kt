package ru.bagrusss.generator.react.kotlin.model

abstract class ReactModelBuilder {

    internal var protoClassFullName = ""

    private val readableMapClass = "com.facebook.react.bridge.ReadableMap"

    fun protoClassFullName(protoClassFullName: String) = apply {
        this.protoClassFullName = protoClassFullName
    }

    abstract fun build(): ReactModel

}