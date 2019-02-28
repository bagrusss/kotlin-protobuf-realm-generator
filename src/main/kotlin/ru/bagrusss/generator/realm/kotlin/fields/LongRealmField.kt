package ru.bagrusss.generator.realm.kotlin.fields

/**
 * Created by bagrusss on 12.07.17
 */
class LongRealmField(builder: Builder): PrimitiveRealmField<LongRealmField>(builder) {

    override val getFieldType = "kotlin.Long"

    class Builder internal constructor(): RealmFieldBuilder<LongRealmField>() {

        init {
            initializer(0L)
        }

        override fun build() = LongRealmField(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}