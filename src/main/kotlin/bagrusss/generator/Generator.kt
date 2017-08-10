package bagrusss.generator

import com.google.protobuf.compiler.PluginProtos
import com.squareup.kotlinpoet.*
import java.io.InputStream
import java.io.PrintStream

class Generator(private val input: InputStream,
                private val output: PrintStream,
                private val params: Array<String>) {

    private companion object {

        @JvmField val packageName = "com.serenity.data_impl.realm.model"
        @JvmField var protoPackageName = ""
        @JvmField var protoFilePackage = ""
        @JvmField val prefix = "Realm"

    }

    fun generateRealmPrimitive(clazz: ClassName, defValue: Any): PluginProtos.CodeGeneratorResponse.File {
        val className = "$prefix${clazz.simpleName()}"

        val realmTypeFile = PluginProtos.CodeGeneratorResponse
                                        .File
                                        .newBuilder()
                                        .setName("$className.kt")

        val classBuilder = TypeSpec.classBuilder(ClassName.bestGuess(className))
        val fieldBuilder = PropertySpec.builder("value", clazz, KModifier.OPEN)
                                       .mutable(true)
                                       .initializer("%L", defValue)

        classBuilder.addProperty(fieldBuilder.build())
                    .addModifiers(KModifier.OPEN)
                    .superclass(ClassName.bestGuess("io.realm.RealmObject"))
                    .addFun(FunSpec.constructorBuilder().build())
                    .addFun(FunSpec.constructorBuilder()
                                   .addParameter(ParameterSpec.builder("value", clazz).build())
                                   .addStatement("this.value = value")
                                   .build())

        val content = KotlinFile.builder(packageName, className)
                                .addType(classBuilder.build())
                                .build()
                                .toJavaFileObject()
                                .getCharContent(true)
                                .toString()

        realmTypeFile.content = content
        return realmTypeFile.build()
    }

    fun generate() {
        Logger.prepare()
        Logger.log("args: $params\n")

        val response = PluginProtos.CodeGeneratorResponse.newBuilder()
        val request = PluginProtos.CodeGeneratorRequest.parseFrom(input)

        response.addFile(generateRealmPrimitive(INT, 0))
        response.addFile(generateRealmPrimitive(LONG, 0L))
        response.addFile(generateRealmPrimitive(FLOAT, "0f"))
        response.addFile(generateRealmPrimitive(DOUBLE, 0.0))
        response.addFile(generateRealmPrimitive(ClassName("kotlin", "String"), "\"\""))
        response.addFile(generateRealmPrimitive(BOOLEAN, false))

        request.protoFileList.forEach { protoFile ->
            protoPackageName = protoFile.options.javaPackage
            protoFilePackage = protoFile.`package`
            Logger.log("proto package ${protoFile.`package`}")
            protoFile.messageTypeList.forEach {
                if (it.hasOptions() /*&& it.options.hasExtension(SwiftDescriptor.swiftMessageOptions)*/) {
                    //if (it.hasOptions() && it.options.hasField(SwiftDescriptor.SwiftFileOptions.getDescriptor().fields.first { it.jsonName.contains("generate_realm_object", true) })) {
                    //parseCurrent(it, response)
                    Logger.log("proto full name ${it.name}")
                }
            }
        }
        response.build().writeTo(output)
    }
}