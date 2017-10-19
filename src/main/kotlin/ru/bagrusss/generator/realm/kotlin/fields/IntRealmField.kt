package ru.bagrusss.generator.realm.kotlin.fields

/**
 * Created by bagrusss on 12.07.17
 */
class IntRealmField private constructor(builder: Builder) : KotlinPrimitiveRealmField<IntRealmField>(builder) {

    override fun getFieldType() = "kotlin.Int"

    class Builder internal constructor(): RealmFieldBuilder<IntRealmField>() {

        init {
            initializer(0)
        }

        override fun build(): IntRealmField {
            return IntRealmField(this)
        }

    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }
}