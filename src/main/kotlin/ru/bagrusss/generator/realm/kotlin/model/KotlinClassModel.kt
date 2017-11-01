package ru.bagrusss.generator.realm.kotlin.model

import ru.bagrusss.generator.realm.kotlin.fields.KotlinRealmField
import com.squareup.kotlinpoet.*
import ru.bagrusss.generator.realm.kotlin.RealmModelBuilder
import ru.bagrusss.generator.realm.kotlin.fields.LinkingObjectsRealmField

/**
 * Created by bagrusss on 10.08.17
 */
class KotlinClassModel private constructor(builder: BuilderRealm): KotlinRealmModel(builder) {

    private val classNameBuilder = TypeSpec.classBuilder(builder.realmClassName)
                                           .addModifiers(KModifier.OPEN)
                                           .superclass(ClassName.bestGuess("io.realm.RealmObject"))

    private val toProtoMethodBuilder = FunSpec.builder("toProto")
                                              .returns(ClassName("", builder.protoClassFullName))

    private val realmProtoConstructor = FunSpec.constructorBuilder()
                                               .addParameter("protoModel", ClassName("", builder.protoClassFullName))

    private val body: String

    private val isMap = builder.isMap

    class BuilderRealm internal constructor(): RealmModelBuilder() {

        override fun build() = KotlinClassModel(this)
    }

    init {
        toProtoMethodBuilder.addStatement("val p = ${builder.protoClassFullName}.newBuilder()")

        builder.fields
               .map { it as KotlinRealmField }
               .forEach {
                   classNameBuilder.addProperty(it.getPropSpec())
                   toProtoMethodBuilder.addStatement(it.toProtoInitializer)
                   realmProtoConstructor.addStatement(it.fromProtoInitializer)
               }

        builder.linkedObjects
               .map { it as LinkingObjectsRealmField }
               .forEach {
                   classNameBuilder.addProperty(it.getPropSpec())
               }

        toProtoMethodBuilder.addStatement("return p.build()")

        if (!isMap) {
            classNameBuilder.addFun(toProtoMethodBuilder.build())
            classNameBuilder.addFun(realmProtoConstructor.build())
        }

        val realmDefaultConstructor = FunSpec.constructorBuilder()
                                             .build()
        classNameBuilder.addFun(realmDefaultConstructor)

        body = KotlinFile.builder(builder.realmPackageName, builder.realmClassName)
                         .addType(classNameBuilder.build())
                         .build()
                         .toJavaFileObject()
                         .getCharContent(true)
                         .toString()
    }

    override fun getModelBody() = body
}