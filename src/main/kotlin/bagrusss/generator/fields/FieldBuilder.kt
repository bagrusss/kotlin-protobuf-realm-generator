package bagrusss.generator.fields


/**
 * Created by bagrusss on 12.07.17
 */
abstract class FieldBuilder<T> {

    var optional: Boolean = false
    var repeated: Boolean = false
    var initializerArgs: Array<out Any>? = null
    var initializerFormat = "%L"
    var typePrefix = ""
    var typeSuffix = ""
    var fullProtoTypeName = ""
    var fieldName = ""
    var generateToProto = true
    var generateFromProto = true
    var primaryKey = false

    abstract fun build(): T

    fun nullable(isOptional: Boolean) = apply {
        if (isOptional) {
            initializerFormat = "%L"
            initializerArgs = arrayOf("null")
        }
        optional = isOptional
    }

    fun repeated(isRepeated: Boolean) = apply {
        if (isRepeated) {
            nullable(true)
        }
        repeated = isRepeated
    }

    fun initializer(initializerFormat: String, vararg initializerArgs: Any) = apply {
        if (!initializerFormat.contains("%"))
            throw IllegalStateException("format must contain % symbol")
        this.initializerFormat = initializerFormat
        this.initializerArgs = initializerArgs
    }

    fun initializer(initializer: String) = apply {
        initializer("%L", initializer)
    }

    fun prefix(prefix: String) = apply {
        this.typePrefix = prefix
    }

    fun suffix(suffix: String) = apply {
        this.typeSuffix = suffix
    }

    fun fullProtoTypeName(name: String) = apply {
        this.fullProtoTypeName = name
    }

    fun fieldName(name: String) = apply {
        this.fieldName = name
    }

    fun generateToProto(generate: Boolean) = apply {
        this.generateToProto = generate
    }

    fun generateFromProto(generate: Boolean) = apply {
        this.generateFromProto = generate
    }

    fun primaryKey(primary: Boolean) = apply {
        this.primaryKey = primary
    }


}