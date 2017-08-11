package bagrusss.generator.kotlin.model

import bagrusss.generator.fields.Field
import bagrusss.generator.model.ModelBuilder
import java.util.*

abstract class KotlinModelBuilder(realmPackage: String,
                                  realmClass: String,
                                  protoClass: String): ModelBuilder(realmPackage, realmClass, protoClass) {

    val fieldsList: LinkedList<Field<*>> = LinkedList()

    fun <T> addField(field: Field<T>) {
        fieldsList.add(field)
    }
}