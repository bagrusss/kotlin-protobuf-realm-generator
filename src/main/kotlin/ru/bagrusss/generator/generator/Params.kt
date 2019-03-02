package ru.bagrusss.generator.generator

import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.nio.file.FileSystems

abstract class Params<P>(builder: Builder<P>) {

    @JvmField val inputStream: InputStream  = builder.inputStream
    @JvmField val outputStream: PrintStream = builder.outputStream
    @JvmField val targetPath: String        = builder.targetPath
    @JvmField val targetPackage: String     = builder.targetPackage

    abstract class Builder<P> {

        @JvmField internal var inputStream: InputStream = System.`in`
        @JvmField internal var outputStream: PrintStream = System.out
        @JvmField internal var targetPath: String = "${FileSystems.getDefault().getPath(".")}${File.separator}result"
        @JvmField internal var targetPackage: String = "result"


        fun inputStream(inputStream: InputStream) = apply {
            this.inputStream = inputStream
        }

        fun inputStream(outputStream: PrintStream) = apply {
            this.outputStream = outputStream
        }

        fun targetPath(targetPath: String) = apply {
            this.targetPath = targetPath
        }

        fun targetPackage(targetPackage: String) = apply {
            this.targetPackage = targetPackage
        }

        abstract fun build(): P

    }

}