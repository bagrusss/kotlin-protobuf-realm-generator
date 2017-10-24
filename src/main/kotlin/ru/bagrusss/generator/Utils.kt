package ru.bagrusss.generator

object Utils {

    const val toMapMethod = "toWritableMap"
    const val fromMapMethod = "fromReadableMap"

    const val writableMapClass = "com.facebook.react.bridge.WritableMap"
    const val readableMapClass = "com.facebook.react.bridge.ReadableMap"

    fun getList(fieldName: String): String {
        return fieldName[0].toUpperCase() + fieldName.substring(1) + "List"
    }

    fun getCount(fieldName: String): String {
        return fieldName[0].toUpperCase() + fieldName.substring(1) + "Count"
    }

    fun getHas(fieldName: String): String {
        return "has" + fieldName[0].toUpperCase() + fieldName.substring(1)
    }
}