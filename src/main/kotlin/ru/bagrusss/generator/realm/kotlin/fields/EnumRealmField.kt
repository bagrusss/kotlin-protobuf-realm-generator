package ru.bagrusss.generator.realm.kotlin.fields

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec


/**
 * Created by bagrusss on 12.07.17
 */
class EnumRealmField private constructor(builder: Builder): PrimitiveRealmField<EnumRealmField>(builder) {

    class Builder internal constructor(): RealmFieldBuilder<EnumRealmField>() {

        override fun build() = EnumRealmField(this)

    }

    override fun getFieldType() = "kotlin.Int"

    override fun getPropSpec(): PropertySpec {
        return if (!repeated) {
            PropertySpec.builder(fieldName, ClassName.bestGuess(getFieldType()))
                        .mutable(true)
                        .initializer("%L", -1)
                        .build().apply {
                val fromProtoBuilder = StringBuilder()
                val toProtoBuilder = StringBuilder()
                fromProtoInitializer = fromProtoBuilder.append("if (")
                                                       .append(protoConstructorParameter)
                                                       .append(".has")
                                                       .append(fieldName[0].toUpperCase())
                                                       .append(fieldName.substring(1))
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
                                                   .append(".forNumber(")
                                                   .append(fieldName)
                                                   .append(")\n")
                                                   .toString()
            }
        } else super.getPropSpec()
    }

    override fun repeatedToProtoInitializer() = "$protoFullTypeName.forNumber(it.value)"

    override fun repeatedFromProtoInitializer() = "it.number"

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}