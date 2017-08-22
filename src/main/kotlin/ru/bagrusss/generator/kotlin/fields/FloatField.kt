package bagrusss.generator.kotlin.fields

import bagrusss.generator.fields.FieldBuilder

/**
 * Created by bagrusss on 12.07.17
 */
class FloatField private constructor(builder: Builder): KotlinPrimitiveField<FloatField>(builder) {

    override fun getFieldType() = "kotlin.Float"

    class Builder internal constructor(): FieldBuilder<FloatField>() {

        init {
            initializer("0f")
        }


        override fun build() = FloatField(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = BoolField.Builder()
    }
}