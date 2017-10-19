package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.FieldBuilder

class BoolField private constructor(builder: Builder): Field<BoolField>(builder) {

    override fun isPrimitive() = true

    class Builder: FieldBuilder<BoolField>() {

        override fun build() = BoolField(this)

    }

}