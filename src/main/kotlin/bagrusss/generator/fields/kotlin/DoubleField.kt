package bagrusss.generator.fields.kotlin

import bagrusss.generator.fields.FieldBuilder

/**
 * Created by bagrusss on 12.07.17
 */
class DoubleField private constructor(builder: Builder): KotlinPrimitiveField<DoubleField>(builder) {

    override fun getFieldType() = "kotlin.Double"

    class Builder: FieldBuilder<DoubleField>() {

        override fun build() = DoubleField(this)

    }

}