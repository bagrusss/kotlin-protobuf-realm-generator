package bagrusss.generator.model

import bagrusss.generator.fields.Field


/**
 * Created by bagrusss on 10.08.17
 */
class KotlinModel private constructor(builder: Builder) {

    inner class Builder(val className: String) {

        fun <T> addField(field: Field<T>) {

        }

        fun build(): KotlinModel {
            return KotlinModel(this)
        }
    }
}