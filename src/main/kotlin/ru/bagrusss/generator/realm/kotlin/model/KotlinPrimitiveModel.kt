package ru.bagrusss.generator.realm.kotlin.model

import com.squareup.kotlinpoet.*

class KotlinPrimitiveModel(realmPackage: String,
                           prefix: String,
                           primitiveClassName: ClassName,
                           defValue: Any): KotlinRealmModel(realmPackage, prefix + primitiveClassName.simpleName) {

    private val body: String

    init {
        val classBuilder = TypeSpec.classBuilder(className)
        val fieldBuilder = PropertySpec.builder("value", primitiveClassName)
                                       .mutable(true)
                                       .initializer("%L", defValue)

        classBuilder.addProperty(fieldBuilder.build())
                    .addModifiers(KModifier.OPEN)
                    .superclass(ClassName.bestGuess("io.realm.RealmObject"))
                    .addFunction(FunSpec.constructorBuilder()
                                        .build())
                    .addFunction(FunSpec.constructorBuilder()
                                        .addParameter(ParameterSpec.builder("value", primitiveClassName)
                                                                   .build())
                                        .addStatement("this.value = value")
                                        .build())

        body = FileSpec.builder(realmPackage, className.simpleName)
                       .addType(classBuilder.build())
                       .build()
                       .toJavaFileObject()
                       .getCharContent(true)
                       .toString()
    }

    override fun getModelBody() = body

}