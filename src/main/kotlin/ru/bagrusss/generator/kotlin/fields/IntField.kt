package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.FieldBuilder

/**
 * Created by bagrusss on 12.07.17
 */
class IntField private constructor(builder: Builder) : KotlinPrimitiveField<IntField>(builder) {

    override fun getFieldType() = "kotlin.Int"

    class Builder internal constructor(): FieldBuilder<IntField>() {

        init {
            initializer(0)
        }

        override fun build(): IntField {
            return IntField(this)
        }

    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }
}