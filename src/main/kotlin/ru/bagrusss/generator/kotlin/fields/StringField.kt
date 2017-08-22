package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.FieldBuilder


/**
 * Created by bagrusss on 12.07.17
 */
class StringField private constructor(builder: Builder): KotlinPrimitiveField<StringField>(builder) {

    override fun getFieldType() = "kotlin.String"

    class Builder internal constructor(): FieldBuilder<StringField>() {

        init {
            initializer("\"\"")
        }

        override fun build() = StringField(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = BoolField.Builder()
    }

}