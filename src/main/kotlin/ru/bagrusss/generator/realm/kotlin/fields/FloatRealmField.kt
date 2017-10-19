package ru.bagrusss.generator.realm.kotlin.fields

/**
 * Created by bagrusss on 12.07.17
 */
class FloatRealmField private constructor(builder: Builder): KotlinPrimitiveRealmField<FloatRealmField>(builder) {

    override fun getFieldType() = "kotlin.Float"

    class Builder internal constructor(): RealmFieldBuilder<FloatRealmField>() {

        init {
            initializer("0f")
        }


        override fun build() = FloatRealmField(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }
}