package ru.bagrusss.generator.react.kotlin.model

import com.squareup.kotlinpoet.FunSpec
import ru.bagrusss.generator.Logger
import ru.bagrusss.generator.Utils
import ru.bagrusss.generator.react.FunParameter
import ru.bagrusss.generator.react.ReactModel
import ru.bagrusss.generator.react.ReactModelBuilder
import ru.bagrusss.generator.react.kotlin.KotlinFunModel
import ru.bagrusss.generator.react.kotlin.field.ReactField

class KotlinReactModel private constructor(builder: Builder): ReactModel<FunSpec>(builder) {

    private val parameter = "map"

    private val isMap = builder.isMap

    init {
        val toWritableBuilder = KotlinFunModel.Builder()
                                              .name("${builder.protoClassFullName}.toWritableMap")
                                              .returns(Utils.writableMapClass)


        val fromReadableBuilder = KotlinFunModel.Builder()
                                                .name("${builder.protoClassFullName}.Builder.fromReadableMap")
                                                .addParameter(FunParameter(parameter, Utils.readableMapClass))
                                                .returns(builder.protoClassFullName)

        val toWritableBodyBuilder = StringBuilder()
        val fromReadableBodyBuilder = StringBuilder()

        toWritableBodyBuilder.append("return ")
                             .append(argumentsClass)
                             .append(".createMap().apply {\n")

        fromReadableBodyBuilder.append("return ")
                               .append(builder.protoClassFullName)
                               .append(".newBuilder().run {\n")

        builder.fieldsList.map { it as ReactField<*> }
                          .forEach {
                              if (!it.needSkip()) {
                                  toWritableBodyBuilder.append('\t')
                                                       .append(it.toMapInitializer())
                                                       .append('\n')

                                  fromReadableBodyBuilder.append('\t')
                                                         .append(it.fieldName)
                                                         .append(" = ")
                                                         .append(it.fromMapInitializer())
                                                         .append('\n')
                              } else {
                                  Logger.log("skip ${builder.protoClassFullName}")
                              }
                          }

        toWritableBodyBuilder.append("}\n")
        fromReadableBodyBuilder.append("\n\tbuild()\n}\n")

        toWritableMapFun = toWritableBuilder.body(toWritableBodyBuilder.toString())
                                            .build()
        fromReadableMapFun = fromReadableBuilder.body(fromReadableBodyBuilder.toString())
                                                .build()

        Logger.log("${builder.protoClassFullName} toMap $toWritableMapFun\n fromMap $fromReadableMapFun")

    }

    override fun getMapFunctions() = if (!isMap) Pair(toWritableMapFun, fromReadableMapFun) else null

    class Builder: ReactModelBuilder() {

        override fun build() = KotlinReactModel(this)

    }

}