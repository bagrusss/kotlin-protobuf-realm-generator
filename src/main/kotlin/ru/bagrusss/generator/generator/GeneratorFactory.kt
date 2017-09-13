package ru.bagrusss.generator.generator

import ru.bagrusss.generator.kotlin.KotlinEntityFactory
import java.io.InputStream
import java.io.PrintStream

class GeneratorFactory(configs: Config,
                       input: InputStream,
                       output: PrintStream) {

    val generator: DefaultRealmGenerator

    init {
        when (configs.lang) {

            Lang.KOTLIN -> {
                generator = KotlinGenerator(input,
                                            output,
                                            configs.realmPath,
                                            configs.realmPackage,
                                            configs.prefix,
                                            KotlinEntityFactory(configs.serializer))
            }
            Lang.JAVA -> throw UnsupportedOperationException("Java not supported yet!")
        }
    }
}