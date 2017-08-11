package bagrusss.generator.fields.kotlin

import bagrusss.generator.fields.FieldBuilder

/**
 * Created by bagrusss on 12.07.17
 */
class BoolField private constructor(builder: Builder): KotlinPrimitiveField<BoolField>(builder) {

    override fun getFieldType() = "kotlin.Boolean"

    inner class Builder: FieldBuilder<BoolField>() {

        override fun build() = BoolField(this)

    }

}