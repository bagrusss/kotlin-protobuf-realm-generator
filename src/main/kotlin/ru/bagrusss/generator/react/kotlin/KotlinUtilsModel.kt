package ru.bagrusss.generator.react.kotlin

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import ru.bagrusss.generator.react.UtilsModelBuilder
import ru.bagrusss.generator.react.UtilsModel

class KotlinUtilsModel private constructor(builder: Builder): UtilsModel<FunSpec>(builder) {

    private val body: String

    init {
        val fileBuilder = FileSpec.builder(builder.packageName, builder.fileName)

        builder.functions.forEach { fileBuilder.addFunction(it.getSpec()) }
        val file = fileBuilder.build()
        body = file.toJavaFileObject()
                   .getCharContent(true)
                   .toString()
    }

    override fun getBody() = body


    class Builder: UtilsModelBuilder<FunSpec>() {

        override fun build() = KotlinUtilsModel(this)
    }

}