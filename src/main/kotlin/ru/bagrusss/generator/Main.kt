package ru.bagrusss.generator

import ru.bagrusss.generator.generator.*
import ru.bagrusss.generator.react.kotlin.KotlinReactGenerator


/**
 * Created by bagrusss on 10.04.17
 */

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val currentDir = System.getProperty("user.dir")

        val realmPath = currentDir + "/${System.getenv("realm_path")}"
        val realmPackage = System.getenv("realm_package")

        val generateRealm = System.getenv("isRealm").toBoolean()

        if (generateRealm) {
            val config = RealmConfig.newBuilder()
                                    .realmPath(realmPath)
                                    .realmPackage(realmPackage)
                                    .lang(Lang.KOTLIN)
                                    .serializer(Serializer.PROTOSTUFF)
                                    .build()

            GeneratorFactory(config, System.`in`, System.out).generator.generate()
        } else {
            val reactPath = System.getenv("react_path")
            KotlinReactGenerator(System.`in`, System.out, reactPath).generate()
        }
    }

}