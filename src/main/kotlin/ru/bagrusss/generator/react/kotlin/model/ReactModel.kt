package ru.bagrusss.generator.react.kotlin.model

abstract class ReactModel(builder: ReactModelBuilder) {

    protected val writableMapClass = "com.facebook.react.bridge.WritableMap"
    protected val readableMapClass = "com.facebook.react.bridge.ReadableMap"
    protected val argumentsClass = "com.facebook.react.bridge.Arguments"

    abstract fun getToWritableMapBody(): String
    abstract fun getToProtoBody(): String

}