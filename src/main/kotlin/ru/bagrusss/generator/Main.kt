package ru.bagrusss.generator

import ru.bagrusss.generator.generator.*
import ru.bagrusss.generator.react.kotlin.KotlinReactGenerator
import ru.bagrusss.generator.react.params.ReactParams
import ru.bagrusss.generator.realm.RealmEntityFactory
import ru.bagrusss.generator.realm.kotlin.KotlinRealmEntityFactory
import ru.bagrusss.generator.realm.kotlin.KotlinRealmGenerator
import ru.bagrusss.generator.realm.params.RealmParams


/**
 * Created by bagrusss on 10.04.17
 */

fun main(args: Array<String>) {
    val currentDir = System.getProperty("user.dir")

    val realmPath = currentDir + "/${System.getenv("realm_path")}"
    val realmPackage = System.getenv("realm_package")

    val generateRealm = System.getenv("isRealm").toBoolean()

    val generator: Generator<*>

    if (generateRealm) {
        Logger.prepare("realm.txt")
        val realmParams = RealmParams.newBuilder()
                                     .modelPrefix("Realm")
                                     .targetPackage(realmPackage)
                                     .targetPath(realmPath)
                                     .build()

        val entityFactory = KotlinRealmEntityFactory()
        generator = KotlinRealmGenerator(realmParams, entityFactory)
    } else {
        Logger.prepare("react.txt")
        val reactPath = System.getenv("react_path")

        val params = ReactParams.newBuilder()
                                .className("ConvertUtils")
                                .targetPackage("ru.rocketbank.serenity.react.utils")
                                .targetPath(reactPath)
                                .build()
        generator = KotlinReactGenerator(params)
    }

    generator.generate()

}
