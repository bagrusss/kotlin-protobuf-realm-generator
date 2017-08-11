package bagrusss.generator.fields.kotlin

import bagrusss.generator.fields.Field
import bagrusss.generator.fields.FieldBuilder
import com.squareup.kotlinpoet.PropertySpec

abstract class KotlinField<T>(builder: FieldBuilder<T>): Field<T>(builder) {

    protected val propertySpec by lazy { getPropSpec() }
    protected val kotlinFieldType by lazy { getFieldType() }

    abstract fun getPropSpec(): PropertySpec
}