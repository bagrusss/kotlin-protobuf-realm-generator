package bagrusss.generator.kotlin.fields

import bagrusss.generator.fields.Field
import bagrusss.generator.fields.FieldBuilder

/**
 * Created by bagrusss on 12.07.17
 */
class MessageField private constructor(builder: Builder): KotlinField<MessageField>(builder) {

    override fun isPrimitive() = false

    class Builder: FieldBuilder<MessageField>() {
        override fun build(): Field<MessageField> {
            initializer("$realmPackage.$protoPackage$typePrefix$fullProtoTypeName()")
            return MessageField(this)
        }

    }

    override fun getFieldType() = protoFullTypeName

    override fun toProtoInitializer() = "${if (optional) "it" else fieldName}.toProto()"
    override fun fromProtoInitializer() = "$realmPackage.$protoPackage$typePrefix$protoFullTypeName($protoConstructorParameter.$fieldName)"

    override fun repeatedToProtoInitializer() = "it.toProto()"
    override fun repeatedFromProtoInitializer() = ""

}