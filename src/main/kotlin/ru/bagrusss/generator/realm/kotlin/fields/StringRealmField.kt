package ru.bagrusss.generator.realm.kotlin.fields


/**
 * Created by bagrusss on 12.07.17
 */
class StringRealmField private constructor(builder: Builder): KotlinPrimitiveRealmField<StringRealmField>(builder) {

    override fun getFieldType() = "kotlin.String"

    class Builder internal constructor(): RealmFieldBuilder<StringRealmField>() {

        init {
            initializer("\"\"")
        }

        override fun build() = StringRealmField(this)

    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }

}