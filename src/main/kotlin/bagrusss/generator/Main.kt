package bagrusss.generator


/**
 * Created by bagrusss on 10.04.17
 */

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        OldGenerator(System.`in`, System.out, args).generate()
    }

}