package ru.bagrusss.generator.generator

import java.io.InputStream
import java.io.PrintStream

class GeneratorFactory(configs: Config,
                       input: InputStream,
                       output: PrintStream) {

    val generator: Generator

    init {
        when (configs.lang) {

            Lang.KOTLIN -> {
                generator = KotlinGenerator(input,
                                            output,
                                            configs.realmPath,
                                            configs.realmPackage,
                                            configs.prefix,
                                            configs.serializer)
            }
            Lang.JAVA -> throw UnsupportedOperationException("Java not supported yet!")
        }
    }
}