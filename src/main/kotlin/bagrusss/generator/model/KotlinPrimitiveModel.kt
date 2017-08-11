package bagrusss.generator.model

import com.squareup.kotlinpoet.*

class KotlinPrimitiveModel(private val realmPackage: String,
                           prefix: String,
                           private val primitiveClassName: ClassName,
                           private val defValue: Any): KotlinModel(realmPackage, prefix + primitiveClassName.simpleName()) {

    init {

    }

    override fun getModelBody(): String {
        val classBuilder = TypeSpec.classBuilder(className)
        val fieldBuilder = PropertySpec.builder("value", primitiveClassName, KModifier.OPEN)
                                       .mutable(true)
                                       .initializer("%L", defValue)

        classBuilder.addProperty(fieldBuilder.build())
                    .addModifiers(KModifier.OPEN)
                    .superclass(ClassName.bestGuess("io.realm.RealmObject"))
                    .addFun(FunSpec.constructorBuilder().build())
                    .addFun(FunSpec.constructorBuilder()
                                   .addParameter(ParameterSpec.builder("value", primitiveClassName).build())
                                   .addStatement("this.value = value")
                                   .build())

        return KotlinFile.builder(realmPackage, className.simpleName())
                         .addType(classBuilder.build())
                         .build()
                         .toJavaFileObject()
                         .getCharContent(true)
                         .toString()
    }

}