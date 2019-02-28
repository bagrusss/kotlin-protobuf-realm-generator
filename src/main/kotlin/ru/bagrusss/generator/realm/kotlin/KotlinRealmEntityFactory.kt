package ru.bagrusss.generator.realm.kotlin

import ru.bagrusss.generator.fields.Type
import ru.bagrusss.generator.generator.Serializer
import ru.bagrusss.generator.realm.RealmEntityFactory
import ru.bagrusss.generator.realm.kotlin.fields.*
import ru.bagrusss.generator.realm.kotlin.model.KotlinClassModel

class KotlinRealmEntityFactory(serializer: Serializer) : RealmEntityFactory(serializer) {

    override fun newBuilder(type: Type) = when (type) {
        Type.BOOL       -> BoolRealmField.newBuilder()
        Type.INT        -> IntRealmField.newBuilder()
        Type.LONG       -> LongRealmField.newBuilder()
        Type.FLOAT      -> FloatRealmField.newBuilder()
        Type.DOUBLE     -> DoubleRealmField.newBuilder()
        Type.STRING     -> StringRealmField.newBuilder()
        Type.BYTES      -> ByteArrayRealmField.newBuilder()
        Type.ENUM       -> EnumRealmField.newBuilder()
        Type.MESSAGE    -> MessageRealmField.newBuilder()
        Type.MAP        -> MapRealmField.newBuilder()
    }

    override fun newModelBuilder() = KotlinClassModel.BuilderRealm()

    override fun newLinkedObjectsBuilder() = LinkingObjectsRealmField.Builder()

}