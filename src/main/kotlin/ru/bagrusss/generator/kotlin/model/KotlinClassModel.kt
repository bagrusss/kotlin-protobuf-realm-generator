package ru.bagrusss.generator.kotlin.model

import ru.bagrusss.generator.kotlin.fields.KotlinField
import ru.bagrusss.generator.model.Model
import com.squareup.kotlinpoet.*

/**
 * Created by bagrusss on 10.08.17
 */
class KotlinClassModel private constructor(builder: BuilderRealm): KotlinModel(builder) {

    private val classNameBuilder = TypeSpec.classBuilder(builder.realmClassName)
                                           .addModifiers(KModifier.OPEN)
                                           .superclass(ClassName.bestGuess("io.realm.RealmObject"))

    private val toProtoMethodBuilder = FunSpec.builder("toProto")
                                              .returns(ClassName("", builder.protoClassFullName))

    private val realmProtoConstructor = FunSpec.constructorBuilder()
                                               .addParameter("protoModel", ClassName("", builder.protoClassFullName))

    private val body: String

    private val isMap = builder.isMap


    class BuilderRealm: KotlinRealmModelBuilder() {

        internal var isMap = false

        override fun isMap(isMap: Boolean) = apply {
            this.isMap = isMap
        }

        override fun build(): Model {
            return KotlinClassModel(this)
        }
    }

    init {
        toProtoMethodBuilder.addStatement("val p = ${builder.protoClassFullName}.newBuilder()")

        builder.fieldsList
               .map { it as KotlinField }
               .forEach {
                   classNameBuilder.addProperty(it.getPropSpec())
                   toProtoMethodBuilder.addStatement(it.toProtoInitializer)
                   realmProtoConstructor.addStatement(it.fromProtoInitializer)
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