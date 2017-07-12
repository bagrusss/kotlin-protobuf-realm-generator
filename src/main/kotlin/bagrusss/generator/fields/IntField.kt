package bagrusss.generator.fields

/**
 * Created by bagrusss on 12.07.17
 */
class IntField private constructor(builder: Builder) : PrimitiveField<IntField>(builder) {

    override fun getFieldType() = "kotlin.Int"

    inner class Builder : FieldBuilder<IntField>() {

        override fun build(): IntField {
            return IntField(this)
        }

    }
}