package bagrusss.generator.fields

/**
 * Created by bagrusss on 12.07.17
 */
class FloatField private constructor(builder: Builder): PrimitiveField<FloatField>(builder) {

    override fun getFieldType() = "kotlin.Float"

    class Builder: FieldBuilder<FloatField>() {

        override fun build() = FloatField(this)

    }
}