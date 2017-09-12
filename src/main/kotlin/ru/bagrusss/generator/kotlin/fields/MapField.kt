package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.FieldBuilder

class MapField private constructor(builder: Builder): KotlinField<MapField>(builder) {

    override fun getFieldType() = protoFullTypeName

    override fun isPrimitive() = false

    class Builder: FieldBuilder<MapField>() {

        override fun build(): Field<MapField> {
            initializer("$realmPackage.$protoPackage$typePrefix$fullProtoTypeName()")
            return MapField(this)
        }

    }
}