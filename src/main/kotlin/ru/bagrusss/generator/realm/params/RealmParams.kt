package ru.bagrusss.generator.realm.params

import ru.bagrusss.generator.generator.Params

class RealmParams private constructor(builder: Builder): Params<RealmParams>(builder) {

    @JvmField val prefix        = builder.prefix
    @JvmField val suffix        = builder.suffix

    class Builder internal constructor(): Params.Builder<RealmParams>() {

        @JvmField internal var prefix: String = "Realm"
        @JvmField internal var suffix: String = ""
        @JvmField internal var realmPackage: String = "realm"

        fun modelPrefix(modelPrefix: String) = apply {
            this.prefix = modelPrefix
        }

        fun modelSuffix(modelSuffix: String) = apply {
            this.suffix = modelSuffix
        }

        fun realmPath(realmPath: String) = apply {
            this.targetPath = realmPath
        }

        override fun build() = RealmParams(this)

    }

    companion object {
        @JvmStatic fun newBuilder() = Builder()
    }

}