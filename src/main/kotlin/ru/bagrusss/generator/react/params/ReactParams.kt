package ru.bagrusss.generator.react.params

import ru.bagrusss.generator.generator.Params

class ReactParams private constructor(builder: Builder): Params<ReactParams>(builder) {

    @JvmField val className = builder.className

    class Builder: Params.Builder<ReactParams>() {

        @JvmField internal var className: String = "ConvertUtils"

        fun className(className: String) = apply {
            this.className = className
        }

        override fun build() = ReactParams(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}