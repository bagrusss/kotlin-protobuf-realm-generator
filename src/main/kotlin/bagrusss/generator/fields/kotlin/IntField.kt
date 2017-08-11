package bagrusss.generator.fields.kotlin

import bagrusss.generator.fields.FieldBuilder

/**
 * Created by bagrusss on 12.07.17
 */
class IntField private constructor(builder: Builder) : KotlinPrimitiveField<IntField>(builder) {

    override fun getFieldType() = "kotlin.Int"

    class Builder : FieldBuilder<IntField>() {

        override fun build(): IntField {
            return IntField(this)
        }

    }
}