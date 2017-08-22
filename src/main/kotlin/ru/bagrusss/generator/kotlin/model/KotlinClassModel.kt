package ru.bagrusss.generator.kotlin.model

import ru.bagrusss.generator.kotlin.fields.KotlinField
import ru.bagrusss.generator.model.Model
import com.squareup.kotlinpoet.*

/**
 * Created by bagrusss on 10.08.17
 */
class KotlinClassModel private constructor(builder: Builder): KotlinModel(builder) {

    private val classNameBuilder = TypeSpec.classBuilder(builder.realmClassName)
                                           .addModifiers(KModifier.OPEN)
                                           .superclass(ClassName.bestGuess("io.realm.RealmObject"))

    private val toProtoMethodBuilder = FunSpec.builder("toProto")
                                              .returns(ClassName.bestGuess(builder.protoClassFullName))

    private val realmProtoConstructor = FunSpec.constructorBuilder()
                                               .addParameter("protoModel", ClassName.bestGuess(builder.protoClassFullName))

    private val body: String


    class Builder(packageName: String, className: String, protoClassName: String): KotlinModelBuilder(packageName, className, protoClassName) {

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

        classNameBuilder.addFun(toProtoMethodBuilder.build())
        classNameBuilder.addFun(realmProtoConstructor.build())

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