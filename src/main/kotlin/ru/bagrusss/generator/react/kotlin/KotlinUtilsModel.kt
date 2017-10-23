package ru.bagrusss.generator.react.kotlin

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KotlinFile
import ru.bagrusss.generator.react.UtilsModelBuilder
import ru.bagrusss.generator.react.UtilsModel

class KotlinUtilsModel private constructor(builder: Builder): UtilsModel<FunSpec>(builder) {

    private val body: String

    init {
        val fileBuilder = KotlinFile.builder(builder.packageName, builder.fileName)
        builder.fromReadableMapFunctions.forEach {
            fileBuilder.addFun(it.getSpec() as FunSpec)
        }
        builder.toWritableMapFunctions.forEach {
            fileBuilder.addFun(it.getSpec() as FunSpec)
        }
        body = fileBuilder.toString()
    }

    class Builder: UtilsModelBuilder<FunSpec>() {

        override fun build() = KotlinUtilsModel(this)
    }

    override fun getBody() = body

}