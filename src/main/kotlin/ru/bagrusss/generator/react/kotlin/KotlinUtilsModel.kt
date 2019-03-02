package ru.bagrusss.generator.react.kotlin

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import ru.bagrusss.generator.react.UtilsModelBuilder
import ru.bagrusss.generator.react.UtilsModel

class KotlinUtilsModel private constructor(builder: Builder): UtilsModel<FunSpec>(builder) {

    private val contentBody: String

    override val fileName = ""

    override val body
        get() = contentBody

    init {
        val fileBuilder = FileSpec.builder(builder.packageName, builder.fileName)

        builder.functions.forEach { fileBuilder.addFunction(it.getSpec()) }
        val file = fileBuilder.build()
        contentBody = file.toJavaFileObject()
                          .getCharContent(true)
                          .toString()
    }

    class Builder: UtilsModelBuilder<FunSpec>() {

        override fun build() = KotlinUtilsModel(this)
    }

}