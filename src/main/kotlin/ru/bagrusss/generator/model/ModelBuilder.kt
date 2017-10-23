package ru.bagrusss.generator.model

import ru.bagrusss.generator.fields.Field


abstract class ModelBuilder {

    internal var protoClassFullName = ""

    fun protoClassFullName(protoClassFullName: String) = apply {
        this.protoClassFullName = protoClassFullName
    }

    abstract fun isMap(isMap: Boolean): ModelBuilder

    abstract fun <T> addField(field: Field<T>): ModelBuilder

    abstract fun build(): Model

}