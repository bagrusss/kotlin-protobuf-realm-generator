package ru.bagrusss.generator.react.kotlin.model

abstract class ReactModel(builder: ReactModelBuilder) {

    abstract fun getToWritableMapBody(): String
    abstract fun getToProtoBody(): String

}