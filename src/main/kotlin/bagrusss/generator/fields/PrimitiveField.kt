package bagrusss.generator.fields

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 */
 abstract class PrimitiveField<T>(builder: FieldBuilder<T>): Field<T>(builder) {

    override fun getPropSpec(): PropertySpec {
        val type = ClassName.bestGuess(kotlinFieldType)
        val propSpecBuilder = PropertySpec.builder(fieldName, type)
                                          .addModifiers(KModifier.OPEN)

        val toProtoBuilder = StringBuilder()

        if (primaryKey)
            propSpecBuilder.addAnnotation(ClassName.bestGuess("io.realm.annotations.PrimaryKey"))

        if (nullable) {
            propSpecBuilder.nullable(true)
                           .initializer("%L", "null")
            if (!repeated) {
                toProtoBuilder.append(fieldName)
                              .append("?.let {\n\t")
                              .append("p.")
                              .append(fieldName)
                              .append(" = ")
                              .append(fieldName)
                              .append("}\n")
            } else {

            }
        }

        toProtoInitializer = toProtoBuilder.toString()

        return propSpecBuilder.build()
    }



}