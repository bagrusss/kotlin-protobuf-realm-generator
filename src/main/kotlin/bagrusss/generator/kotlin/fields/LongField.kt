package bagrusss.generator.kotlin.fields

import bagrusss.generator.fields.FieldBuilder

/**
 * Created by bagrusss on 12.07.17
 */
class LongField(builder: Builder): KotlinPrimitiveField<LongField>(builder) {

    override fun getFieldType() = "kotlin.Long"

    init {

    }

    class Builder: FieldBuilder<LongField>() {

        init {
            initializer(0L)
        }

        override fun build() = LongField(this)

    }

}