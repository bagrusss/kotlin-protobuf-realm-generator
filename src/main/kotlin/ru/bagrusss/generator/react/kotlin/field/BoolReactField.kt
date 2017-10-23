package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.FieldBuilder

class BoolReactField private constructor(builder: Builder): Field<BoolReactField>(builder) {

    override fun isPrimitive() = true

    class Builder: FieldBuilder<BoolReactField>() {

        override fun build() = BoolReactField(this)

    }

}