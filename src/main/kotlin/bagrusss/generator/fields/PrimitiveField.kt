package bagrusss.generator.fields

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 */
 abstract class PrimitiveField<T>(builder: FieldBuilder<T>): Field<T>(builder) {

    override fun getPropSpec(): PropertySpec {

        val propSpecBuilder = if (!repeated) {
                                  PropertySpec.builder(fieldName, ClassName.bestGuess(kotlinFieldType))
                              } else {
                                  val realmListType = ClassName.bestGuess("io.realm.RealmList")
                                  val typedList = ParameterizedTypeName.get(realmListType,
                                                                            ClassName.bestGuess(typePrefix + kotlinFieldType.split(".")
                                                                                                                                           .last()))
                                  PropertySpec.builder(fieldName, typedList)
                              }.addModifiers(KModifier.OPEN)

        val toProtoBuilder = StringBuilder()

        if (primaryKey)
            propSpecBuilder.addAnnotation(ClassName.bestGuess("io.realm.annotations.PrimaryKey"))

        if (nullable) {
            propSpecBuilder.nullable(true)
                           .initializer("%L", "null")

            toProtoBuilder.append(fieldName)
                          .append("?.let {\n\t")
                          .append("p.")

            if (!repeated) {
                toProtoBuilder.append(fieldName)
                              .append(" = ")
                              .append(fieldName)
            } else {
                toProtoBuilder.append("addAll")
                              .append(fieldName.substring(0, 1).toUpperCase())
                              .append(fieldName.substring(1))
                              .append('(')
                              .append(fieldName)
                              .append(".map { it.value })\n")
            }
            toProtoBuilder.append("}")

        } else {
            propSpecBuilder.nullable(false)
                           .initializer(initializerFormat, initializerArgs)

            toProtoBuilder.append(fieldName)
                          .append("p.")
                          .append(" = ")
                          .append(fieldName)
                          .append('\n')
        }

        toProtoInitializer = toProtoBuilder.toString()

        return propSpecBuilder.build()
    }



}