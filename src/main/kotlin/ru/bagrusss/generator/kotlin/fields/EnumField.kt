package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.FieldBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec


/**
 * Created by bagrusss on 12.07.17
 */
class EnumField private constructor(builder: Builder): KotlinPrimitiveField<EnumField>(builder) {

    class Builder internal constructor(): FieldBuilder<EnumField>() {

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
                fromProtoInitializer = fromProtoBuilder.append("if (")
                                                       .append(protoConstructorParameter)
                                                       .append(".has")
                                                       .append(fieldName[0].toUpperCase())
                                                       .append(fieldName.subSequence(1, fieldName.length))
                                                       .append("()) ")
                                                       .append(fieldName)
                                                       .append(" = ")
                                                       .append(protoConstructorParameter)
                                                       .append('.')
                                                       .append(fieldName)
                                                       .append(".number\n")
                                                       .toString()

                toProtoInitializer = toProtoBuilder.append("if (")
                                                   .append(fieldName)
                                                   .append(" != -1) ")
                                                   .append("p.")
                                                   .append(fieldName)
                                                   .append(" = ")
                                                   .append(protoFullTypeName)
                                                   //.append(".valueOf(")
                                                   .append(".forNumber(")
                                                   .append(fieldName)
                                                   .append(")\n")
                                                   .toString()
            }
        } else super.getPropSpec()
    }

    //override fun repeatedToProtoInitializer() = "$protoFullTypeName.valueOf(it.value)"
    override fun repeatedToProtoInitializer() = "$protoFullTypeName.forNumber(it.value)"

    override fun repeatedFromProtoInitializer() = "it.number"

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}