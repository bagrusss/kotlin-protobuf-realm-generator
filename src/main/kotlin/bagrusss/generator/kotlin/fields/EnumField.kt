package bagrusss.generator.kotlin.fields

import bagrusss.generator.fields.FieldBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec


/**
 * Created by bagrusss on 12.07.17
 */
class EnumField private constructor(builder: Builder): KotlinPrimitiveField<EnumField>(builder) {

    class Builder: FieldBuilder<EnumField>() {

        override fun build() = EnumField(this)

    }

    override fun getFieldType() = "kotlin.Int"


    override fun getPropSpec(): PropertySpec {
        return if (!repeated) {
            PropertySpec.builder(fieldName, ClassName.bestGuess(kotlinFieldType), KModifier.OPEN)
                        .mutable(true)
                        .initializer("%L", -1)
                        .build().apply {
                val fromProtoBuilder = StringBuilder()
                val toProtoBuilder = StringBuilder()
                fromProtoInitializer = fromProtoBuilder.append(fieldName)
                                                       .append(" = ")
                                                       .append(protoConstructorParameter)
                                                       .append('.')
                                                       .append(fieldName)
                                                       .append(".number\n")
                                                       .toString()

                toProtoInitializer = toProtoBuilder.append("p.")
                                                   .append(fieldName)
                                                   .append(" = ")
                                                   .append(protoFullTypeName)
                                                   .append(".valueOf(")
                                                   .append(fieldName)
                                                   .append(")\n")
                                                   .toString()
            }
        } else super.getPropSpec()
    }

    override fun repeatedToProto() = "$protoFullTypeName.valueOf(it.value)"

    override fun repeatedFromProto() = "it.number"

}