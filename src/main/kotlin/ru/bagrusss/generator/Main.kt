package ru.bagrusss.generator

import ru.bagrusss.generator.generator.*


/**
 * Created by bagrusss on 10.04.17
 */

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val currentDir = System.getProperty("user.dir")

        val realmPath = currentDir + "/${System.getenv("realm_path")}"
        val realmPackage = System.getenv("realm_package")

        val config = Config.newBuilder()
                           .realmPath(realmPath)
                           .realmPackage(realmPackage)
                           .lang(Lang.KOTLIN)
                           .serializer(Serializer.PROTOSTUFF)
                           .build()

        GeneratorFactory(config, System.`in`, System.out).generator.generate()

    }

}