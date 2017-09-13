package ru.bagrusss.generator.kotlin

import ru.bagrusss.generator.fields.FieldBuilder
import ru.bagrusss.generator.fields.EntityFactory
import ru.bagrusss.generator.fields.TYPE
import ru.bagrusss.generator.generator.Serializer
import ru.bagrusss.generator.kotlin.fields.*
import ru.bagrusss.generator.kotlin.model.KotlinClassModel
import ru.bagrusss.generator.model.RealmModelBuilder

class KotlinEntityFactory(serializer: Serializer) : EntityFactory(serializer) {

    override fun  createBuilder(type: TYPE): FieldBuilder<*> {
        return when(type) {
            TYPE.BOOL -> BoolField.newBuilder()
            TYPE.INT -> IntField.newBuilder()
            TYPE.LONG -> LongField.newBuilder()
            TYPE.FLOAT -> FloatField.newBuilder()
            TYPE.DOUBLE -> DoubleField.newBuilder()
            TYPE.STRING -> StringField.newBuilder()
            TYPE.BYTES -> ByteArrayField.newBuilder()
            TYPE.ENUM -> EnumField.newBuilder()
            TYPE.MESSAGE -> MessageField.newBuilder()
            TYPE.MAP -> MapField.newBuilder()
        }
    }

    override fun createModelBuilder(): RealmModelBuilder {
        return KotlinClassModel.BuilderRealm()
    }

}