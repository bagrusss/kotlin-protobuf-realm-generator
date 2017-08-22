package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.FieldBuilder

/**
 * Created by bagrusss on 12.07.17
 */
class BoolField private constructor(builder: Builder): KotlinPrimitiveField<BoolField>(builder) {

    override fun getFieldType() = "kotlin.Boolean"

    class Builder internal constructor(): FieldBuilder<BoolField>() {

        init {
            initializer(false)
        }

        override fun build() = BoolField(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}