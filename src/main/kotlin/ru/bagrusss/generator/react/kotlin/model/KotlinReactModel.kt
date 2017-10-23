package ru.bagrusss.generator.react.kotlin.model

import com.squareup.kotlinpoet.FunSpec
import ru.bagrusss.generator.react.FunParameter
import ru.bagrusss.generator.react.ReactModel
import ru.bagrusss.generator.react.ReactModelBuilder
import ru.bagrusss.generator.react.kotlin.KotlinFunModel
import ru.bagrusss.generator.react.kotlin.field.ReactField

class KotlinReactModel private constructor(builder: Builder): ReactModel<FunSpec>(builder) {

    private val parameter = "map"

    init {
        val toWritableBuilder = KotlinFunModel.Builder()
                                              .name("${builder.protoClassFullName}.toWritableMap")
                                              .returns(writableMapClass)


        val fromReadableBuilder = KotlinFunModel.Builder()
                                                .name("${builder.protoClassFullName}.Builder.fromReadableMap")
                                                .addParameter(FunParameter(parameter, readableMapClass))
                                                .returns(builder.protoClassFullName)

        val toWritableBodyBuilder = StringBuilder()
        val fromReadableBodyBuilder = StringBuilder()

        toWritableBodyBuilder.append("return ")
                             .append(argumentsClass)
                             .append("createMap().apply {\n")

        fromReadableBodyBuilder.append("return ")
                               .append(builder.protoClassFullName)
                               .append(".newBuilder().run {\n")

        builder.fieldsList.map { it as ReactField<*> }
                          .forEach {
                              toWritableBodyBuilder.append(it.putMethodName())
                                                   .append("(\"")
                                                   .append(it.fieldName)
                                                   .append("\", ")
                                                   .append(it.fieldName)
                                                   .append(")\n")

                              fromReadableBodyBuilder.append(it.fieldName)
                                      .append(" = ")
                                      .append(parameter)
                                      .append('.')
                                      .append(it.getMethodName())
                                      .append("(\"")
                                      .append(it.fieldName)
                                      .append("\")\n")
                          }

        toWritableBodyBuilder.append("}\n")
        fromReadableBodyBuilder.append("\nbuild()\n}\n")

        toWritableMapFun = toWritableBuilder.body(toWritableBodyBuilder.toString())
                                            .build()
        fromReadableMapFun = fromReadableBuilder.body(fromReadableBodyBuilder.toString())
                                                .build()

    }

    override fun getMapFunctions() = Pair(toWritableMapFun, fromReadableMapFun)

    class Builder: ReactModelBuilder() {

        override fun build() = KotlinReactModel(this)

    }

}