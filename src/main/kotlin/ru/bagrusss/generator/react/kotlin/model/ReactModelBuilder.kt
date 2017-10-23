package ru.bagrusss.generator.react.kotlin.model

abstract class ReactModelBuilder<I> {

    internal var protoClassFullName = ""
    internal var fileName = ""

    fun protoClassFullName(protoClassFullName: String) = apply {
        this.protoClassFullName = protoClassFullName
    }

    fun fileName(fileName: String) = apply {
        this.fileName = fileName
    }

    abstract fun build(): ReactModel<I>

}