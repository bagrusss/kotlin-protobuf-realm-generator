package ru.bagrusss.generator.realm.kotlin.fields

/**
 * Created by bagrusss on 12.07.17
 */
class DoubleRealmField private constructor(builder: Builder): KotlinPrimitiveRealmField<DoubleRealmField>(builder) {

    override fun getFieldType() = "kotlin.Double"

    class Builder internal constructor(): RealmFieldBuilder<DoubleRealmField>() {

        init {
            initializer(0.0)
        }

        override fun build() = DoubleRealmField(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}