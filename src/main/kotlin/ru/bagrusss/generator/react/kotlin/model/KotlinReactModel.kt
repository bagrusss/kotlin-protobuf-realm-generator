package ru.bagrusss.generator.react.kotlin.model

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec

class KotlinReactModel(builder: ReactModelBuilder): ReactModel(builder) {

    private val toWritableMapBuilder = FunSpec.builder("${builder.protoClassFullName}.toWritableMap")
                                              .returns(ClassName("", writableMapClass))

    private val toProtoBuildert = FunSpec.builder("$readableMapClass.toProto")
                                         .addParameter("builder", ClassName("", "${builder.protoClassFullName}.Builder"))
                                         .returns(ClassName("", builder.protoClassFullName))


    override fun getToWritableMapBody() = ""

    override fun getToProtoBody() = ""

}