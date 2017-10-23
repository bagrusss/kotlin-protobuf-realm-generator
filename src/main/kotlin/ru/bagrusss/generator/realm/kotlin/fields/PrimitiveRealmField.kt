package ru.bagrusss.generator.realm.kotlin.fields

/**
 * Created by bagrusss on 12.07.17
 * used for primitive types and strings
 */
 abstract class PrimitiveRealmField<T>(builder: RealmFieldBuilder<T>): KotlinRealmField<T>(builder) {

    override fun isPrimitive() = true

    override fun repeatedFromProtoInitializer() = "it"

    override fun repeatedToProtoInitializer() = "it.value"

    override fun fromProtoInitializer() = "$protoConstructorParameter.$fieldName"

    override fun toProtoInitializer() = if (optional) "it" else fieldName


}