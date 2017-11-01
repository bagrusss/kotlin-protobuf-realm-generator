package ru.bagrusss.generator.realm.kotlin.fields

import com.squareup.kotlinpoet.*

class LinkedObjectsRealmField private constructor(builder: Builder): KotlinRealmField<LinkedObjectsRealmField>(builder) {

    private val propertyName = builder.propertyName

    override fun isPrimitive() = false

    override fun getFieldType() = protoFullTypeName

    override fun getPropSpec(): PropertySpec {
        val realmResultsType = ClassName.bestGuess("io.realm.RealmResults")
        val realmClass = ClassName("", protoFullTypeName)
        val typedResults = ParameterizedTypeName.get(realmResultsType, realmClass)
        val linkedObjectAnnotation = AnnotationSpec.builder(ClassName.bestGuess(linkedObjectAnnotation))
                                                   .addMember("value", "%S", propertyName)
                                                   .build()
        return PropertySpec.builder(fieldName, typedResults)
                           .addAnnotation(linkedObjectAnnotation)
                           .addModifiers(KModifier.OPEN)
                           .mutable(true)
                           .nullable(true)
                           .initializer("%L", "null")
                           .build()
    }

    class Builder internal constructor(): RealmFieldBuilder<LinkedObjectsRealmField>() {

        internal var propertyName = ""

        fun propertyName(propertyName: String) = apply {
            this.propertyName = propertyName
        }

        override fun build() = LinkedObjectsRealmField(this)

    }
}