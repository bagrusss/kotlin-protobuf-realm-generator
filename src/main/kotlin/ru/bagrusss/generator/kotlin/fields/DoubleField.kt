package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.FieldBuilder

/**
 * Created by bagrusss on 12.07.17
 */
class DoubleField private constructor(builder: Builder): KotlinPrimitiveField<DoubleField>(builder) {

    override fun getFieldType() = "kotlin.Double"

    class Builder internal constructor(): FieldBuilder<DoubleField>() {

        init {
            initializer(0.0)
        }

        override fun build() = DoubleField(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}