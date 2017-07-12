package bagrusss.generator.fields

/**
 * Created by bagrusss on 12.07.17
 */
class DoubleField private constructor(builder: Builder): PrimitiveField<DoubleField>(builder) {

    override fun getFieldType() = "kotlin.Double"

    class Builder: FieldBuilder<DoubleField>() {

        override fun build() = DoubleField(this)

    }

}