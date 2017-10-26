package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.Utils
import ru.bagrusss.generator.fields.Field


abstract class ReactField<T: ReactField<T>>(builder: ReactFieldBuilder<T>): Field<T>(builder) {

    private val checkOptionalToMap = "\n\tif (${Utils.getHas(fieldName)}())\n\t\t"
    private val checkOptionalFromMap = "\n\tif (map.hasKey(\"$fieldName\")) \n\t\t"

    abstract fun getReactType(): String
    abstract fun getReactTypeForMap(): String

    open fun needSkip() = false

    protected fun putToArrayMethodName() = "push" + getReactType()
    protected fun getFromArrayMethodName() = "get" + getReactType()

    protected open fun fromMapInit() = fieldName + " = map.get" + getReactType() + "(\"" + fieldName + "\")"
    protected open fun toMapInit() = "put" + getReactType() + "(\"" + fieldName + "\", " + fieldName + ")"

    protected open fun toMapConverter() = "it"

    protected open fun getFromMapInit() = "$protoFullTypeName.newBuilder().${Utils.fromMapMethod}("

    /*в карту
    if (colorsList.isNotEmpty()) {
        val colorsArray = Arguments.createArray()
        colorsList.forEach { colorsArray.pushMap(it.toWritableMap()) }
    }*/

    /*из карты
    val colorsElements = map.getArray("colors")
    for (i in 0 until colorsElements.size()) {
        val element = ru.rocketbank.protomodel.ProtoApi.Color.newBuilder().fromReadableMap(colorsElements.getMap(i))
        addColors(element)
    }*/

    protected open fun toMapRepeated(): String {
        val array = Utils.fieldArray(fieldName)
        return StringBuilder().append("\n\t")
                              .append(Utils.checkListSize(fieldName))
                              .append("{\n\t\t")
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
                              .append(toMapConverter())
                              .append(") }\n\t}")
                              .toString()
    }

    fun toMapInitializer(): String {
        return when {
            repeated -> { toMapRepeated() }
            optional -> checkOptionalToMap + toMapInit()
            else -> toMapInit()
        }
    }

    fun fromMapInitializer(): String {
        return when {
            repeated -> {
                val array = Utils.fieldArray(fieldName)
                val item = "element"
                val builder = StringBuilder().append("\n\tval ")
                                             .append(array)
                                             .append(" = map.getArray(\"")
                                             .append(fieldName)
                                             .append("\")\n")
                                             .append("\tfor (i in 0 until ")
                                             .append(array)
                                             .append(".size()) {\n")
                                             .append("\t\tval ")
                                             .append(item)
                                             .append(" = ")

                if (!isPrimitive()) {
                    builder.append(getFromMapInit())
                }

                builder.append(array)
                       .append('.')
                       .append(getFromArrayMethodName())
                       .append("(i)")

                if (!isPrimitive())
                    builder.append(")")

                builder.append("\n\t\t")
                       .append(Utils.addToArray(fieldName))
                       .append('(')
                       .append(item)
                       .append(")\n\t}")

                builder.toString()
            }
            optional -> checkOptionalFromMap + fromMapInit()
            else -> fromMapInit()
        }
    }

}