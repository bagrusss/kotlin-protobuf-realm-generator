package bagrusss.generator.kotlin.fields

import bagrusss.generator.fields.FieldBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec


/**
 * Created by bagrusss on 12.07.17
 */
class EnumField private constructor(builder: Builder): KotlinField<EnumField>(builder) {

    init {
        val fromProtoBuilder = StringBuilder()
        val toProtoBuilder = StringBuilder()
        if (!repeated) {
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
        } else {
            realmListsInitialize(typePrefix + "Int", toProtoBuilder, fromProtoBuilder, true)
        }
    }

    class Builder: FieldBuilder<EnumField>() {

        override fun build() = EnumField(this)

    }

    override fun getFieldType() = "kotlin.Int"


    override fun getPropSpec(): PropertySpec {
        return PropertySpec.builder(fieldName, ClassName.bestGuess(kotlinFieldType), KModifier.OPEN)
                           .mutable(true)
                           .build()
    }

}