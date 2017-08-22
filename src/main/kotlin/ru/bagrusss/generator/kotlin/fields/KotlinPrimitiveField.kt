package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.FieldBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 * used for primitive types and strings
 */
 abstract class KotlinPrimitiveField<T>(builder: FieldBuilder<T>): KotlinField<T>(builder) {

    override fun isPrimitive() = true

    override fun repeatedFromProtoInitializer() = "it"

    override fun repeatedToProtoInitializer() = "it.value"

    override fun fromProtoInitializer() = "$protoConstructorParameter.$fieldName"

    override fun toProtoInitializer() = if (optional) "it" else fieldName


}