package bagrusss.generator.kotlin.fields

import bagrusss.generator.fields.FieldBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 * used for primitive types and strings
 */
 abstract class KotlinPrimitiveField<T>(builder: FieldBuilder<T>): KotlinField<T>(builder) {

    override fun getPropSpec(): PropertySpec {

        var classTypeName: String? = null

        val propSpecBuilder = if (!repeated) {
                                  PropertySpec.builder(fieldName, ClassName.bestGuess(kotlinFieldType).apply {
                                      classTypeName = this.simpleName()
                                  })
                              } else {
                                  val realmListType = ClassName.bestGuess("io.realm.RealmList")
                                  val typedList = ParameterizedTypeName.get(realmListType,
                                                                            ClassName.bestGuess(typePrefix + kotlinFieldType.split(".")
                                                                                                                                           .last())
                                                                                                                                           .apply {
                                                                                                                                               classTypeName = this.simpleName()
                                                                                                                                           })
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
                          .append("?.let {\n\t")
                          .append("p.")

            realmProtoConstructorBuilder.append("if (")
                                        .append(protoConstructorParameter)

            if (!repeated) {
                toProtoBuilder.append(fieldName)
                              .append(" = it")

                realmProtoConstructorBuilder.append(".has")
                                            .append(fieldName.substring(0, 1).toUpperCase())
                                            .append(fieldName.substring(1))
                                            .append("())\n\t")
                                            .append(fieldName)
                                            .append(" = ")
                                            .append(protoConstructorParameter)
                                            .append('.')
                                            .append(fieldName)

            } else {
                toProtoBuilder.append("addAll")
                              .append(fieldName.substring(0, 1).toUpperCase())
                              .append(fieldName.substring(1))
                              .append('(')
                              .append(fieldName)
                              .append(".map { it.value })\n")

                realmProtoConstructorBuilder.append(".")
                                            .append(fieldName.substring(0, 1).toUpperCase())
                                            .append(fieldName.substring(1))
                                            .append("Count > 0) {\n\t")
                                            .append(fieldName)
                                            .append(" = RealmList()")
                                            .append(fieldName)
                                            .append(".addAll(")
                                            .append(protoConstructorParameter)
                                            .append('.')
                                            .append(fieldName)
                                            .append("List.map { ")
                                            .append(typePrefix + classTypeName)
                                            .append("(it) })\n}")

                realmListsInitialize(classTypeName, toProtoBuilder, realmProtoConstructorBuilder, true)
            }
            toProtoBuilder.append("}\n")
            realmProtoConstructorBuilder.append('\n')

        } else {
            propSpecBuilder.nullable(false)
                           .initializer("%L", initializerArgs)

            toProtoBuilder.append("p.")
                          .append(fieldName)
                          .append(" = ")
                          .append(fieldName)
                          .append('\n')

            realmProtoConstructorBuilder.append(fieldName)
                                        .append(" = ")
                                        .append(protoConstructorParameter)
                                        .append(".")
                                        .append(fieldName)
                                        .append('\n')
        }


        toProtoInitializer = toProtoBuilder.toString()
        fromProtoInitializer = realmProtoConstructorBuilder.toString()

        return propSpecBuilder.build()
    }



}