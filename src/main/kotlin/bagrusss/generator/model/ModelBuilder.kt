package bagrusss.generator.model

import bagrusss.generator.fields.kotlin.KotlinField
import java.util.*

abstract class ModelBuilder(val realmPackageName: String,
                            val realmClassName: String,
                            val protoClassFullName: String) {

    val fieldsList: LinkedList<KotlinField<*>> = LinkedList()

    fun <T> addField(field: KotlinField<T>) = apply {
        fieldsList.add(field)
    }

    abstract fun build(): Model

}