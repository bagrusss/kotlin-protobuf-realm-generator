package ru.bagrusss.generator.realm.kotlin

import ru.bagrusss.generator.fields.TYPE
import ru.bagrusss.generator.realm.RealmEntityFactory
import ru.bagrusss.generator.realm.kotlin.fields.RealmFieldBuilder
import ru.bagrusss.generator.generator.Serializer
import ru.bagrusss.generator.realm.kotlin.fields.*
import ru.bagrusss.generator.realm.kotlin.model.KotlinClassModel

class KotlinRealmEntityFactory(serializer: Serializer) : RealmEntityFactory(serializer) {

    override fun  createBuilder(type: TYPE): RealmFieldBuilder<*> {
        return when(type) {
            TYPE.BOOL -> BoolRealmField.newBuilder()
            TYPE.INT -> IntRealmField.newBuilder()
            TYPE.LONG -> LongRealmField.newBuilder()
            TYPE.FLOAT -> FloatRealmField.newBuilder()
            TYPE.DOUBLE -> DoubleRealmField.newBuilder()
            TYPE.STRING -> StringRealmField.newBuilder()
            TYPE.BYTES -> ByteArrayRealmField.newBuilder()
            TYPE.ENUM -> EnumRealmField.newBuilder()
            TYPE.MESSAGE -> MessageRealmField.newBuilder()
            TYPE.MAP -> MapRealmField.newBuilder()
        }
    }

    override fun createModelBuilder(): RealmModelBuilder {
        return KotlinClassModel.BuilderRealm()
    }

}