package ru.bagrusss.generator.model

import ru.bagrusss.generator.fields.Field
import java.util.*


abstract class ModelBuilder<I> {

    internal var protoClassFullName = ""

    val fieldsList: LinkedList<Field<*>> = LinkedList()

    fun <T> addField(field: Field<T>) = apply {
        fieldsList.add(field)
    }

    fun protoClassFullName(protoClassFullName: String) = apply {
        this.protoClassFullName = protoClassFullName
    }

    abstract fun isMap(isMap: Boolean): ModelBuilder<I>

    abstract fun build(): Model<I>

}