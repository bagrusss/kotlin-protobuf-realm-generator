package ru.bagrusss.generator.model

abstract class ModelBuilder(val realmPackageName: String,
                            val realmClassName: String,
                            val protoClassFullName: String) {

    abstract fun build(): Model

}