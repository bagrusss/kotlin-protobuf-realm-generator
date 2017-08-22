package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.FieldBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec

abstract class KotlinField<T>(builder: FieldBuilder<T>): Field<T>(builder) {

    protected val propertySpec by lazy { getPropSpec() }
    protected val kotlinFieldType by lazy { getFieldType() }

    open fun repeatedToProtoInitializer() = ""
    open fun repeatedFromProtoInitializer() = ""

    open fun getPropSpec(): PropertySpec {

        var classTypeName: String? = null

        val propSpecBuilder = if (!repeated) {
                                  PropertySpec.builder(fieldName, if (isPrimitive())
                                                                      ClassName.bestGuess(kotlinFieldType).apply {
                                                                                classTypeName = this.simpleName()
                                                                      }
                                                                  else ClassName("", "$realmPackage.$protoPackage$typePrefix$protoFullTypeName"))
                              } else {
                                  val realmListType = ClassName.bestGuess("io.realm.RealmList")
                                  val className =  if (isPrimitive())
                                                         ClassName(realmPackage, typePrefix + kotlinFieldType.split(".")
                                                                                                                         .last()
                                                         ).apply {
                                                             classTypeName = this.simpleName()
                                                         }
                                                    else ClassName("", "$realmPackage.$protoPackage$typePrefix$protoFullTypeName").apply {
                                                           classTypeName = this.simpleName()
                                                         }
                                  val typedList = ParameterizedTypeName.get(realmListType, className)
                                  PropertySpec.builder(fieldName, typedList)
                              }.addModifiers(KModifier.OPEN)
                               .mutable(true)

        val toProtoBuilder = StringBuilder()
        val realmProtoConstructorBuilder = StringBuilder()


        if (primaryKey)
            propSpecBuilder.addAnnotation(ClassName.bestGuess("io.realm.annotations.PrimaryKey"))

        if (optional) {
            propSpecBuilder.nullable(true)
                           .initializer("%L", "null")

            toProtoBuilder.append(fieldName)
                          .append("?.let { ")
                          .append("p.")

            realmProtoConstructorBuilder.append("if (")
                                        .append(protoConstructorParameter)

            if (!repeated) {
                toProtoBuilder.append(fieldName)
                              .append(" = ")
                              .append(toProtoInitializer())

                realmProtoConstructorBuilder.append(".has")
                                            .append(fieldName.substring(0, 1).toUpperCase())
                                            .append(fieldName.substring(1))
                                            .append("())\n")
                                            .append(fieldName)
                                            .append(" = ")
                                            .append(fromProtoInitializer())

            } else {
                toProtoBuilder.append("addAll")
                              .append(fieldName.substring(0, 1).toUpperCase())
                              .append(fieldName.substring(1))
                              .append("(it.map { ${repeatedToProtoInitializer()} })")


                realmProtoConstructorBuilder.append(".")
                                            .append(fieldName)
                                            .append("Count > 0) {\n")
                                            .append(fieldName)
                                            .append(" = RealmList()\n")
                                            .append(fieldName)
                                            .append("!!.addAll(")
                                            .append(protoConstructorParameter)
                                            .append('.')
                                            .append(fieldName)
                                            .append("List.map { ")
                                            .append(classTypeName)
                                            .append("(${repeatedFromProtoInitializer()}) })}")

            }
            toProtoBuilder.append(" }")
        } else {
            propSpecBuilder.nullable(false)
                           .initializer("%L", initializerArgs)

            toProtoBuilder.append("p.")
                          .append(fieldName)
                          .append(" = ")
                          .append(toProtoInitializer())

            realmProtoConstructorBuilder.append(fieldName)
                                        .append(" = ")
                                        .append(fromProtoInitializer())
        }


        toProtoInitializer = toProtoBuilder.toString()
        fromProtoInitializer = realmProtoConstructorBuilder.toString()

        return propSpecBuilder.build()
    }

    open fun toProtoInitializer() = ""
    open fun fromProtoInitializer() = ""
}