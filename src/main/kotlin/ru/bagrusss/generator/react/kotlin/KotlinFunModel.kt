package ru.bagrusss.generator.react.kotlin

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import ru.bagrusss.generator.react.FunModel
import ru.bagrusss.generator.react.FunModelBuilder

class KotlinFunModel private constructor(builder: Builder): FunModel<FunSpec>(builder) {

    private val spec: FunSpec

    init {
        val specBuilder = FunSpec.builder(builder.name)
                                 .returns(ClassName("", builder.returns))

        builder.parameters.forEach {
            specBuilder.addParameter(ParameterSpec.builder(it.name, ClassName("", it.type))
                                                  .build())
        }
        spec = specBuilder.build()
    }

    override fun getSpec() = spec


    class Builder: FunModelBuilder<FunSpec>() {
        override fun build() = KotlinFunModel(this)
    }
}