package ru.bagrusss.generator.react.kotlin.model

abstract class Model {

    abstract fun getToWritableMapBody(): String
    abstract fun getToProto(): String

}