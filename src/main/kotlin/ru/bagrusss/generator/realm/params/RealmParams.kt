package ru.bagrusss.generator.realm.params

import ru.bagrusss.generator.generator.Params

class RealmParams private constructor(builder: Builder): Params<RealmParams>(builder) {

    @JvmField val prefix        = builder.prefix
    @JvmField val suffix        = builder.suffix
    @JvmField val realmPackage  = builder.realmPackage
    @JvmField val realmPath     = builder.realmPath

    class Builder internal constructor(): Params.Builder<RealmParams>() {

        internal var prefix:  String = "Realm"
        internal var suffix:  String = "Realm"
        internal var realmPackage: String = "realm"
        internal var realmPath:    String = "realm"

        fun modelPrefix(modelPrefix: String) = apply {
            this.prefix = modelPrefix
        }

        fun modelSuffix(modelSuffix: String) = apply {
            this.suffix = modelSuffix
        }

        fun realmPackage(realmPackage: String) = apply {
            this.realmPackage = realmPackage
        }

        fun realmPath(realmPath: String) = apply {
            this.realmPath = realmPath
        }

        override fun build() = RealmParams(this)

    }

    companion object {
        @JvmStatic fun newBuilder() = Builder()
    }

}