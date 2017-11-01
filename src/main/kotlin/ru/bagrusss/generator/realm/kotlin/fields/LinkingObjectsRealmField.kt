package ru.bagrusss.generator.realm.kotlin.fields

import com.squareup.kotlinpoet.*

class LinkingObjectsRealmField private constructor(builder: Builder): KotlinRealmField<LinkingObjectsRealmField>(builder) {

    private val propertyName = builder.propertyName

    override fun isPrimitive() = false

    override fun getFieldType() = protoFullTypeName

    override fun getPropSpec(): PropertySpec {
        val realmResultsType = ClassName.bestGuess(realmResultsClass)
        val realmClass = ClassName("", protoFullTypeName)
        val typedResults = ParameterizedTypeName.get(realmResultsType, realmClass)
        val linkedObjectAnnotation = AnnotationSpec.builder(ClassName.bestGuess(linkedObjectAnnotation))
                                                   .addMember("value", "%S", propertyName)
                                                   .build()
        return PropertySpec.builder(fieldName, typedResults)
                           .addAnnotation(linkedObjectAnnotation)
                           .mutable(false)
                           .nullable(true)
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