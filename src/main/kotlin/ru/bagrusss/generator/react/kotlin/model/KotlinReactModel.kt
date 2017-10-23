package ru.bagrusss.generator.react.kotlin.model

import com.squareup.kotlinpoet.FunSpec
import ru.bagrusss.generator.react.FunModel
import ru.bagrusss.generator.react.FunParameter
import ru.bagrusss.generator.react.ReactModel
import ru.bagrusss.generator.react.ReactModelBuilder
import ru.bagrusss.generator.react.kotlin.KotlinFunModel

class KotlinReactModel(builder: Builder): ReactModel(builder) {

    private val toWritableMapFun: FunModel<FunSpec>
    private val fromReadableMapFun: FunModel<FunSpec>

    init {
        val toWritableBuilder = KotlinFunModel.Builder()
                                              .name("${builder.protoClassFullName}.toWritableMap")
                                              .returns(writableMapClass)



        val fromReadableBuilder = KotlinFunModel.Builder()
                                                .name("${builder.protoClassFullName}.fromReadableMap")
                                                .addParameter(FunParameter("map", readableMapClass))
                                                .returns(builder.protoClassFullName)

        val toWritableBodyBuilder = StringBuilder()
        val fromReadableBodyBuilder = StringBuilder()

        toWritableBodyBuilder.append("return ")
                             .append(argumentsClass)
                             .append("createMap().apply {\n")

        fromReadableBodyBuilder.append("return ")
                               .append(builder.protoClassFullName)
                               .append(".newBuilder().run {\n")

        builder.fieldsList.forEach {

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