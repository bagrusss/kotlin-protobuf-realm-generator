package ru.bagrusss.generator.generator

abstract class Generator {

    /**
     * Used for filter generator results
     */
    protected var filter : () -> Boolean = { true }

    abstract fun generate()

}