package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.FieldBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 */
class ByteArrayField private constructor(builder: Builder): KotlinField<ByteArrayField>(builder) {

    override fun isPrimitive() = false

    class Builder internal constructor(): FieldBuilder<ByteArrayField>() {

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
                                                      .append(".toByteArray()")
                                                      .toString()

                toProtoInitializer = StringBuilder().append("p.")
                                                    .append(fieldName)
                                                    .append(" = io.protostuff.ByteString.copyFrom(")
                                                    .append(fieldName)
                                                    .append(')')
                                                    .toString()
        }
    }

    companion object {
        @JvmStatic
        fun newBuilder() = BoolField.Builder()
    }

}