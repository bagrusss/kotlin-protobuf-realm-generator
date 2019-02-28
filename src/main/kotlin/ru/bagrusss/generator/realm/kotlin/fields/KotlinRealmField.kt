package ru.bagrusss.generator.realm.kotlin.fields

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

abstract class KotlinRealmField<T>(builder: RealmFieldBuilder<T>): RealmField<T>(builder) {

    protected val generateToProto      = builder.generateToProto
    protected val generateFromProto    = builder.generateFromProto

    private var classTypeName = ""

    open fun repeatedToProtoInitializer() = ""
    open fun repeatedFromProtoInitializer() = ""

    //TODO
    protected abstract fun getFieldType(): String

    open fun getPropSpec(): PropertySpec {

        val propSpecBuilder = if (!repeated) {
                                  val type = if (isPrimitive())
                                                ClassName.bestGuess(getFieldType()).apply { classTypeName = simpleName }
                                             else ClassName("", "$realmPackage.$protoPackage$typePrefix$protoFullTypeName")
                                  PropertySpec.builder(fieldName, type.copy(nullable = optional))
                              } else {
                                  val realmListType = ClassName.bestGuess(realmListClass)
                                  val className =  if (isPrimitive())
                                                         ClassName(realmPackage, typePrefix + getFieldType().split(".")
                                                                                                                        .last()
                                                         ).apply {
                                                             classTypeName = this.simpleName
                                                         }
                                                    else ClassName("", "$realmPackage.$protoPackage$typePrefix$protoFullTypeName").apply {
                                                           classTypeName = this.simpleName
                                                         }
                                  val typedList = realmListType.parameterizedBy(className)
                                  PropertySpec.builder(fieldName, typedList.copy(nullable = optional))
                              }.mutable(true)

        val toProtoBuilder = StringBuilder()
        val realmProtoConstructorBuilder = StringBuilder()


        if (primaryKey)
            propSpecBuilder.addAnnotation(ClassName.bestGuess(primaryKeyAnnotation))

        if (indexed)
            propSpecBuilder.addAnnotation(ClassName.bestGuess(indexAnnotation))

        if (optional) {
            propSpecBuilder.initializer("null")

            toProtoBuilder.append(fieldName)
                          .append("?.let { ")

            realmProtoConstructorBuilder.append("if (")
                                        .append(protoConstructorParameter)

            if (!repeated) {
                toProtoBuilder.append("p.")
                              .append(fieldName)
                              .append(" = ")
                              .append(toProtoInitializer())

                realmProtoConstructorBuilder.append(".has")
                                            .append(fieldName[0].toUpperCase())
                                            .append(fieldName.substring(1))
                                            .append("())\n")
                                            .append(fieldName)
                                            .append(" = ")
                                            .append(fromProtoInitializer())

            } else {
                val toProtoFill = repeatedToProtoFill()
                toProtoBuilder.append(toProtoFill)

                val constructorFill = repeatedFromProtoFill()
                realmProtoConstructorBuilder.append(constructorFill)

            }
            toProtoBuilder.append(" }")
        } else {
            propSpecBuilder.initializer("%L", initializerArgs)

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

    open fun repeatedToProtoFill(): String {
        return StringBuilder().append("p.addAll")
                              .append(fieldName[0].toUpperCase())
                              .append(fieldName.substring(1))
                              .append("(it.map { ${repeatedToProtoInitializer()} })")
                              .toString()
    }

    open fun repeatedFromProtoFill(): String {
        return StringBuilder().append(".")
                              .append(fieldName)
                              .append("Count > 0) {\n")
                              .append(fieldName)
                              .append(" = ")
                              .append(realmListClass)
                              .append("()\n")
                              .append(fieldName)
                              .append("!!.addAll(")
                              .append(protoConstructorParameter)
                              .append('.')
                              .append(fieldName)
                              .append("List.map { ")
                              .append(classTypeName)
                              .append("(${repeatedFromProtoInitializer()}) })}")
                              .toString()
    }
}