package ru.bagrusss.generator.realm.kotlin.fields

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 */
class ByteArrayRealmField private constructor(builder: Builder): KotlinRealmField<ByteArrayRealmField>(builder) {

    override fun isPrimitive() = false

    class Builder internal constructor(): RealmFieldBuilder<ByteArrayRealmField>() {

        override fun build() = ByteArrayRealmField(this)

    }

    override fun getFieldType() = "kotlin.ByteArray"

    override fun getPropSpec(): PropertySpec {
        return PropertySpec.builder(fieldName, ClassName.bestGuess(getFieldType()))
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
                                                    //.append(" = io.protostuff.ByteString.copyFrom(")
                                                    .append(" = com.google.protobuf.ByteString.copyFrom(")
                                                    .append(fieldName)
                                                    .append(')')
                                                    .toString()
        }
    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}