package ru.bagrusss.generator.react

import java.util.*

abstract class FunModelBuilder<T> {

    internal var name = ""
    internal var returns = ""
    internal val parameters = LinkedList<FunParameter>()
    internal var body = ""

    fun name(name: String) = apply {
        this.name = name
    }

    fun returns(returns: String) = apply {
        this.returns = returns
    }

    fun addParameter(param: FunParameter) = apply {
        parameters.add(param)
    }

    fun body(body: String) = apply {
        this.body = body
    }

    abstract fun build(): FunModel<T>

}