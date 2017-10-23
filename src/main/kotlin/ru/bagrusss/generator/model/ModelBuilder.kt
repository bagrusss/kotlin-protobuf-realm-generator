package ru.bagrusss.generator.model

import ru.bagrusss.generator.fields.Field


abstract class ModelBuilder<I> {

    internal var protoClassFullName = ""

    fun protoClassFullName(protoClassFullName: String) = apply {
        this.protoClassFullName = protoClassFullName
    }

    abstract fun isMap(isMap: Boolean): ModelBuilder<I>

    abstract fun <T> addField(field: Field<T>): ModelBuilder<I>

    abstract fun build(): Model<I>

}