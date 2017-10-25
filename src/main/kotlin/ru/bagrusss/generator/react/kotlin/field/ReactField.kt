package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.Utils
import ru.bagrusss.generator.fields.Field


abstract class ReactField<T: ReactField<T>>(builder: ReactFieldBuilder<T>): Field<T>(builder) {

    private val checkOptional = "if (${Utils.getHas(fieldName)}())\n\t\t"

    abstract fun getReactType(): String
    open fun needSkip() = false

    protected fun putToArrayMethodName() = "push" + getReactType()
    protected fun getFromArrayMethodName() = "get" + getReactType()

    protected open fun fromMapInit() = "map.get" + getReactType() + "(\"" + fieldName + "\")"
    protected open fun toMapInit() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ")"

    protected open fun toMapConverter() = "it"

    /*в карту
    if (colorsList.isNotEmpty()) {
        val colorsArray = Arguments.createArray()
        colorsList.forEach { colorsArray.pushMap(it.toWritableMap()) }
    }*/

    /*из карты
    val colorsElements = map.getArray("colors")
    for (i in 0 until colorsElements.size()) {
        val element = ru.rocketbank.protomodel.ProtoApi.Color.newBuilder()
        addColors(element.fromReadableMap(colorsElements.getMap(i)))
    }*/

    fun toMapInitializer(): String {
        return when {
            repeated -> {
                val array = Utils.fieldArray(fieldName)
                StringBuilder().append(Utils.checkListSize(fieldName))
                               .append(" {\n\t\t")
                               .append("val ")
                               .append(array)
                               .append(" = ")
                               .append(Utils.createArray)
                               .append("\n\t\t")
                               .append(Utils.getList(fieldName))
                               .append(".forEach { ")
                               .append(array)
                               .append('.')
                               .append(putToArrayMethodName())
                               .append('(')
                               //.append(if (isPrimitive()) "" else ".${Utils.toMapMethod}()")
                               .append(toMapConverter())
                               .append(") }\n\t}")
                               .toString()
            }
            optional -> checkOptional + toMapInit()
            else -> toMapInit()
        }
    }

    fun fromMapInitializer() = fromMapInit()

}