package ru.bagrusss.generator.generator

import java.io.InputStream
import java.io.PrintStream

abstract class Params<B: Params.Builder>(builder: B) {

    @JvmField val iputStream: InputStream = builder.inputStream
    @JvmField val outputStream: PrintStream = builder.outputStream

    abstract class Builder {

        internal var inputStream: InputStream = System.`in`
        internal var outputStream: PrintStream = System.out

        fun inputStream(inputStream: InputStream) = apply {
            this.inputStream = inputStream
        }

        fun inputStream(outputStream: PrintStream) = apply {
            this.outputStream = outputStream
        }
        
    }
}