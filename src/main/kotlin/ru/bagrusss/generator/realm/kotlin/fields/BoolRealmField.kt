package ru.bagrusss.generator.realm.kotlin.fields

/**
 * Created by bagrusss on 12.07.17
 */
class BoolRealmField private constructor(builder: Builder): PrimitiveRealmField<BoolRealmField>(builder) {

    override fun getFieldType() = "kotlin.Boolean"

    class Builder internal constructor(): RealmFieldBuilder<BoolRealmField>() {

        init {
            initializer(false)
        }

        override fun build() = BoolRealmField(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}