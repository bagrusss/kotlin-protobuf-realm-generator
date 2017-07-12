package bagrusss.generator.fields

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 */
class EnumField private constructor(builder: Builder): Field<EnumField>(builder) {

    override fun getPropSpec(): PropertySpec {
        val type = ClassName.bestGuess(kotlinFieldType)
        val builder = PropertySpec.builder(fieldName, type)
        return builder.build()
    }

    override fun getFieldType() = "kotlin.Int"

    class Builder: FieldBuilder<EnumField>() {

        override fun build() = EnumField(this)

    }

}