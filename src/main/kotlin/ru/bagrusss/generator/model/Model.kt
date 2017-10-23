package ru.bagrusss.generator.model

abstract class Model<out I> {
    abstract fun getImpl(): I
}