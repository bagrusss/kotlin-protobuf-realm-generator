package bagrusss.generator.fields

/**
 * Created by bagrusss on 12.07.17
 */
class LongField(builder: Builder): PrimitiveField<LongField>(builder) {

    override fun getFieldType() = "kotlin.Long"

    class Builder: FieldBuilder<LongField>() {

        override fun build() = LongField(this)

    }

}