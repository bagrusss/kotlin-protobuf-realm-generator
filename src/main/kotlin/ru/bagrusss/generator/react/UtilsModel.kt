package ru.bagrusss.generator.react

import ru.bagrusss.generator.model.Model

abstract class UtilsModel<T>(builder: UtilsModelBuilder<T>): Model() {

    protected var packageName = ""

}