package ru.bagrusss.generator.react

import ru.bagrusss.generator.model.ModelBuilder

abstract class ReactModelBuilder: ModelBuilder() {

    internal var fileName = ""

    fun fileName(fileName: String) = apply {
        this.fileName = fileName
    }

}