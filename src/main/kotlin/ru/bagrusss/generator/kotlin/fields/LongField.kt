package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.FieldBuilder

/**
 * Created by bagrusss on 12.07.17
 */
class LongField(builder: Builder): KotlinPrimitiveField<LongField>(builder) {

    override fun getFieldType() = "kotlin.Long"

    class Builder internal constructor(): FieldBuilder<LongField>() {

        init {
            initializer(0L)
        }

        override fun build() = LongField(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = BoolField.Builder()
    }

}