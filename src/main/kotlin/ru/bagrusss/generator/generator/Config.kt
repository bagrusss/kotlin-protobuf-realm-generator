package ru.bagrusss.generator.generator

class Config private constructor(builder: ConfigBuilder) {

    val lang = builder.lang
    val serializer = builder.serializer
    val realmPath = builder.realmPath
    val realmPackage = builder.realmPackage
    val prefix = builder.prefix

    init {
        if (realmPackage.isEmpty())
            throw IllegalStateException("realmPackage must be set!")

        if (realmPath.isEmpty())
            throw IllegalStateException("realmPath must be set!")
    }

    class ConfigBuilder internal constructor() {

        internal var lang = Lang.KOTLIN
        internal var serializer = Serializer.PROTOSTUFF
        internal var realmPath = ""
        internal var realmPackage = ""
        internal var prefix = "Realm"

        fun lang(lang: Lang) = apply {
            this.lang = lang
        }

        fun serializer(serializer: Serializer) = apply {
            this.serializer = serializer
        }

        fun realmPath(realmPath: String) = apply {
            this.realmPath = realmPath
        }

        fun realmPackage(realmPackage: String) = apply {
            this.realmPackage = realmPackage
        }

        fun prefix(prefix: String) = apply {
            this.prefix = prefix
        }

        fun build(): Config = Config(this)
    }

    companion object {
        @JvmStatic
        fun newBuilder() = ConfigBuilder()
    }

}

enum class Lang {
    KOTLIN,
    JAVA
}

enum class Serializer {
    GOOGLE,
    PROTOSTUFF,
    WIRE,
    REACT
}
