package bagrusss.generator.fields


/**
 * Created by bagrusss on 12.07.17
 */
abstract class FieldBuilder<T> {

    internal var optional: Boolean = false
    internal var repeated: Boolean = false
    internal var initializerArgs: Any? = null
    internal var initializerFormat = "%L"
    internal var typePrefix = ""
    internal var typeSuffix = ""
    internal var fullProtoTypeName = ""
    internal var protoTypeName = ""
    internal var fieldName = ""
    internal var parentName = ""
    internal var generateToProto = true
    internal var generateFromProto = true
    internal var primaryKey = false
    internal var realmPackage = ""
    internal var protoPackage = ""

    abstract fun build(): Field<T>

    fun optional(isOptional: Boolean) = apply {
        if (isOptional) {
            initializerFormat = "%L"
            initializerArgs = "null"
        }
        optional = isOptional
    }

    fun repeated(isRepeated: Boolean) = apply {
        if (isRepeated) {
            optional(true)
        }
        repeated = isRepeated
    }

    fun initializer(initializerFormat: String, initializerArgs: Any) = apply {
        if (!initializerFormat.contains("%"))
            throw IllegalStateException("format must contain % symbol")
        this.initializerFormat = initializerFormat
        this.initializerArgs = initializerArgs
    }

    fun initializer(initializer: Any) = apply {
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

    fun parentName(name: String) = apply {
        this.parentName = name
    }

    fun realmPackage(pkg: String) = apply {
        this.realmPackage = pkg
    }

    fun protoPackage(pkg: String) = apply {
        this.protoPackage = pkg
    }

}