package bagrusss.generator.model

import bagrusss.generator.fields.Field

abstract class ModelBuilder(val packageName: String,
                            val ClassName: String) {

    abstract fun <T> addField(field: Field<T>): ModelBuilder

    abstract fun build(): Model
}