package ru.bagrusss.generator.react.kotlin.model

import ru.bagrusss.generator.model.Model

abstract class ReactModel<I>(builder: ReactModelBuilder<I>): Model<I>() {

    protected val writableMapClass = "com.facebook.react.bridge.WritableMap"
    protected val readableMapClass = "com.facebook.react.bridge.ReadableMap"

    protected val argumentsClass = "com.facebook.react.bridge.Arguments"

    abstract fun getToWritableMapBody(): String
    abstract fun getFromReadableMapBody(): String

}