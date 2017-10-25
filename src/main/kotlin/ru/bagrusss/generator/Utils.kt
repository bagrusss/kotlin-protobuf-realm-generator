package ru.bagrusss.generator

object Utils {

    const val toMapMethod = "toWritableMap"
    const val fromMapMethod = "fromReadableMap"

    const val writableMapClass = "com.facebook.react.bridge.WritableMap"
    const val readableMapClass = "com.facebook.react.bridge.ReadableMap"

    const val writableArrayClass = "com.facebook.react.bridge.WritableArray"
    const val readableArrayClass = "com.facebook.react.bridge.ReadableArray"

    const val createArray = "com.facebook.react.bridge.Arguments.createArray()"

    fun getList(fieldName: String): String {
        return "${fieldName}List"
    }

    fun getCount(fieldName: String): String {
        return fieldName[0].toUpperCase() + fieldName.substring(1) + "Count"
    }

    fun checkListSize(fieldName: String): String {
        return "if (${getList(fieldName)}.isNotEmpty()) "
    }

    fun getHas(fieldName: String): String {
        return "has" + fieldName[0].toUpperCase() + fieldName.substring(1)
    }

    fun fieldArray(fieldName: String) = "${fieldName}Array"
}