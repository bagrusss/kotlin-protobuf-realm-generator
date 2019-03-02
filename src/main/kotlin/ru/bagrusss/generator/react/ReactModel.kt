package ru.bagrusss.generator.react

import ru.bagrusss.generator.model.Model

abstract class ReactModel<T>(builder: ReactModelBuilder) : Model() {

    protected val argumentsClass = "com.facebook.react.bridge.Arguments"

    protected lateinit var toWritableMapFun: FunModel<T>
    protected lateinit var fromReadableMapFun: FunModel<T>

    override val fileName = ""
    override val body = ""

    /**
     * @return first - toWritableMapFun, second - fromReadableMapFun
     */
    abstract fun getMapFunctions(): Pair<FunModel<T>, FunModel<T>>?

}