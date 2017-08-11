package bagrusss.generator.fields.kotlin

import bagrusss.generator.fields.FieldBuilder


/**
 * Created by bagrusss on 12.07.17
 */
class StringField private constructor(builder: Builder): KotlinPrimitiveField<StringField>(builder) {

    override fun getFieldType() = "kotlin.String"

    inner class Builder: FieldBuilder<StringField>() {

        override fun build() = StringField(this)

    }

}