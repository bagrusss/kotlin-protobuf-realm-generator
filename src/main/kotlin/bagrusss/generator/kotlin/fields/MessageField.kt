package bagrusss.generator.kotlin.fields

import bagrusss.generator.fields.FieldBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 */
class MessageField private constructor(builder: Builder): KotlinField<MessageField>(builder) {

    init {
        toProtoInitializer = ""
        fromProtoInitializer = ""
    }

    class Builder: FieldBuilder<MessageField>() {
        override fun build() = MessageField(this)
    }

    override fun getPropSpec(): PropertySpec {
        return PropertySpec.builder(fieldName, ClassName("", "$realmPackage.$protoPackage$protoFullTypeName"))
                           .addModifiers(KModifier.OPEN)
                           .mutable(true)
                           .nullable(true)
                           .initializer("%L", "null")
                           .build()
    }

    override fun getFieldType() = protoFullTypeName

}