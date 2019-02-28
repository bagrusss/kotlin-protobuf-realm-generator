package ru.bagrusss.generator.realm.kotlin.fields

import ru.bagrusss.generator.fields.Field

/**
 * Created by bagrusss on 12.07.17
 */
class MessageRealmField private constructor(builder: Builder): KotlinRealmField<MessageRealmField>(builder) {

    override val getFieldType = protoFullTypeName

    override val isPrimitive = false

    class Builder internal constructor(): RealmFieldBuilder<MessageRealmField>() {

        override fun build(): Field<MessageRealmField> {
            initializer("$realmPackage.$protoPackage$typePrefix$fullProtoTypeName()")
            return MessageRealmField(this)
        }

    }

    override fun toProtoInitializer() = "${if (optional) "it" else fieldName}.toProto()"
    override fun fromProtoInitializer() = "$realmPackage.$protoPackage$typePrefix$protoFullTypeName($protoConstructorParameter.$fieldName)"

    override fun repeatedToProtoInitializer() = "it.toProto()"
    override fun repeatedFromProtoInitializer() = "it"

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}