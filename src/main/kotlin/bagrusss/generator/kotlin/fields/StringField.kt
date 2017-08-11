package bagrusss.generator.kotlin.fields

import bagrusss.generator.fields.FieldBuilder


/**
 * Created by bagrusss on 12.07.17
 */
class StringField private constructor(builder: Builder): KotlinPrimitiveField<StringField>(builder) {

    override fun getFieldType() = "kotlin.String"

    class Builder: FieldBuilder<StringField>() {

        override fun build() = StringField(this)

    }

}