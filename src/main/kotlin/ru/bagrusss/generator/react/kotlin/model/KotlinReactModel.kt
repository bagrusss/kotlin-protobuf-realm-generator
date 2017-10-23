package ru.bagrusss.generator.react.kotlin.model

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec

class KotlinReactModel(builder: ReactModelBuilder<KotlinReactModel>): ReactModel<KotlinReactModel>(builder) {

    override fun getImpl() = this

    private val toWritableMapBuilder = FunSpec.builder("${builder.protoClassFullName}.toWritableMap")
                                              .returns(ClassName("", writableMapClass))

    private val toProtoBuildert = FunSpec.builder("${builder.protoClassFullName}.fromReadableMap")
                                         .addParameter("map", ClassName("", readableMapClass))
                                         .returns(ClassName("", builder.protoClassFullName))

    init {

    }

    override fun getToWritableMapBody() = ""

    override fun getFromReadableMapBody() = ""

}