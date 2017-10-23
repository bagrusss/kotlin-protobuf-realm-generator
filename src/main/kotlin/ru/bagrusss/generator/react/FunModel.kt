package ru.bagrusss.generator.react

abstract class FunModel<out T>(builder: FunModelBuilder<T>) {

    abstract fun getSpec(): T
}