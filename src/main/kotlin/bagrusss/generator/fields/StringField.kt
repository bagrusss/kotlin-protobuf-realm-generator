package bagrusss.generator.fields


/**
 * Created by bagrusss on 12.07.17
 */
class StringField private constructor(builder: StringField.Builder): PrimitiveField<StringField>(builder) {

    override fun getFieldType() = "kotlin.String"

    inner class Builder: FieldBuilder<StringField>() {

        override fun build() = StringField(this)

    }

}