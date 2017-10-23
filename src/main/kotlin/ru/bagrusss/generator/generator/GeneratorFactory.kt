package ru.bagrusss.generator.generator

import ru.bagrusss.generator.realm.kotlin.KotlinRealmEntityFactory
import ru.bagrusss.generator.realm.kotlin.KotlinRealmGenerator
import ru.bagrusss.generator.realm.DefaultRealmGenerator
import ru.bagrusss.generator.realm.kotlin.model.KotlinClassModel
import java.io.InputStream
import java.io.PrintStream

class GeneratorFactory(configs: RealmConfig,
                       input: InputStream,
                       output: PrintStream) {

    val generator: DefaultRealmGenerator<KotlinClassModel>

    init {
        when (configs.lang) {
            Lang.KOTLIN -> {
                generator = KotlinRealmGenerator(input,
                                                 output,
                                                 configs.realmPath,
                                                 configs.realmPackage,
                                                 configs.prefix,
                                                 KotlinRealmEntityFactory(configs.serializer))
            }
            Lang.JAVA -> throw UnsupportedOperationException("Java not supported yet!")
        }
    }
}