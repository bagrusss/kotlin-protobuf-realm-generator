package ru.bagrusss.generator.realm.kotlin.fields

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy


class LinkingObjectsRealmField private constructor(builder: Builder): KotlinRealmField<LinkingObjectsRealmField>(builder) {

    private val propertyName = builder.propertyName

    override fun isPrimitive() = false

    override fun getFieldType() = protoFullTypeName

    override fun getPropSpec(): PropertySpec {
        val realmResultsType = ClassName.bestGuess(realmResultsClass)
        val realmClass = ClassName("", protoFullTypeName)
        val typedResults = realmResultsType.parameterizedBy(realmClass)
        val linkedObjectAnnotation = AnnotationSpec.builder(ClassName.bestGuess(linkedObjectAnnotation))
                                                   .addMember("value", "%S", propertyName)
                                                   .build()
        return PropertySpec.builder(fieldName, typedResults.copy(nullable = true))
                           .addAnnotation(linkedObjectAnnotation)
                           .mutable(false)
                           .initializer("%L", "null")
                           .build()
    }

    class Builder internal constructor(): RealmFieldBuilder<LinkingObjectsRealmField>() {

        internal var propertyName = ""

        fun propertyName(propertyName: String) = apply {
            this.propertyName = propertyName
        }

        override fun build() = LinkingObjectsRealmField(this)

    }
}