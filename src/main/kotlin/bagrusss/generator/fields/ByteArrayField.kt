package bagrusss.generator.fields

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 */
class ByteArrayField private constructor(builder: Builder): Field<ByteArrayField>(builder){

    class Builder: FieldBuilder<ByteArrayField>() {

        override fun build() = ByteArrayField(this)

    }

    override fun getFieldType() = "kotlin.ByteArray"

    override fun getPropSpec(): PropertySpec {
        return PropertySpec.builder(fieldName, ClassName.bestGuess(kotlinFieldType), KModifier.OPEN)
                           .initializer("%L", "ByteArray(0)")
                           .mutable(true)
                           .build().apply {
                fromProtoInitializer = StringBuilder().append(fieldName)
                                                      .append(" = ")
                                                      .append(protoConstructorParameter)
                                                      .append('.')
                                                      .append(fieldName)
                                                      .append(".toByteArray()\n")
                                                      .toString()

                toProtoInitializer = StringBuilder().append("p.")
                                                    .append(fieldName)
                                                    .append(" = io.protostuff.ByteString.copyFrom(")
                                                    .append(fieldName)
                                                    .append(")\n")
                                                    .toString()
        }
    }


}