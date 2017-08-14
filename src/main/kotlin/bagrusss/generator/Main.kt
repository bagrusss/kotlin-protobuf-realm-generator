package bagrusss.generator

import bagrusss.generator.generator.KotlinGenerator


/**
 * Created by bagrusss on 10.04.17
 */

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        //OldGenerator(System.`in`, System.out, args).generate()
        val currentDir = System.getProperty("user.dir")
        KotlinGenerator(System.`in`,
                System.out,
                currentDir + "/${System.getenv("realm_path")}",
                System.getenv("realm_package"),
                "Realm").generate()
    }

}