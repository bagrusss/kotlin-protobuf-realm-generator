package ru.bagrusss.generator.generator

import java.io.InputStream
import java.io.PrintStream

abstract class Params<P>(builder: Builder<P>) {

    @JvmField val inputStream: InputStream = builder.inputStream
    @JvmField val outputStream: PrintStream = builder.outputStream

    abstract class Builder<P> {

        internal var inputStream: InputStream = System.`in`
        internal var outputStream: PrintStream = System.out

        fun inputStream(inputStream: InputStream) = apply {
            this.inputStream = inputStream
        }

        fun inputStream(outputStream: PrintStream) = apply {
            this.outputStream = outputStream
        }

        abstract fun build(): P

    }

}