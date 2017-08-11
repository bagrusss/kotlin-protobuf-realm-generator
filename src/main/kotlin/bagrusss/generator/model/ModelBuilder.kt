package bagrusss.generator.model

import bagrusss.generator.fields.Field

abstract class ModelBuilder(val packageName: String,
                            val className: String) {

    abstract fun <T> addField(field: Field<T>): ModelBuilder

    abstract fun build(): Model

    abstract fun setPrefix(prefix: String): ModelBuilder
}