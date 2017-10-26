package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.Utils
import ru.bagrusss.generator.fields.Type

class MapReactField private constructor(builder: Builder): MessageReactField(builder) {

    private val valueType = builder.valueType


    /* to Map
    val requisitesArray = com.facebook.react.bridge.Arguments.createArray()
    for ((k, v) in requisitesMap) {
        val item = com.facebook.react.bridge.Arguments.createMap()
        item.putString(k, v)
        requisitesArray.pushMap(item)
    }
    putArray("requisites", requisitesArray)
    */

    /* from Map
    val requisitesArray = map.getArray("requisites")
    for (i in 0 until requisitesArray.size()) {
        val requisitesItem = requisitesArray.getMap(i)
        val iterator = requisitesItem.keySetIterator()
        do {
            iterator.nextKey()?.let {
                requisitesMap.put(it, requisitesItem.getString(it))
            }
        } while (iterator.hasNextKey())
    }*/

    override fun fromMapInitializer(): String {
        val array = fieldName + "Array"
        val item = fieldName + "Item"
        val initializer = when (valueType) {
            Type.STRING    -> "$item.getString(it)"
            Type.DOUBLE    -> "$item.getDouble(it)"
            Type.INT       -> "$item.getInt(it)"
            Type.BOOL      -> "$item.getBoolean(it)"
            Type.FLOAT     -> "$item.getDouble(it).toFloat()"
            Type.LONG      -> "$item.getDouble(it).toLong()"
            Type.ENUM      -> "$protoFullTypeName.valueOf($item.getString(it))"
            else           -> "$protoFullTypeName.newBuilder().${Utils.fromMapMethod}($item.getMap(it))"
        }
        return StringBuilder().append("\n\tval ")
                              .append(array)
                              .append(" = map.getArray(\"")
                              .append(fieldName)
                              .append("\")\n\t")
                              .append("for (i in 0 until ")
                              .append(array)
                              .append(".size()) {\n\t\t")
                              .append("val ")
                              .append(item)
                              .append(" = ")
                              .append(array)
                              .append(".getMap(i)\n\t\t")
                              .append("val iterator = ")
                              .append(item)
                              .append(".keySetIterator()\n\t\t")
                              .append("do {\n\t\t\t")
                              .append("iterator.nextKey()?.let { ")
                              .append(fieldName)
                              .append("Map.put(it, ")
                              .append(initializer)
                              .append(") }\n\t\t} while(")
                              .append("iterator.hasNextKey())")
                              .append("\n\t}")
                              .toString()
    }

    override fun toMapInitializer(): String {
        val element = "item"
        val arrayName = fieldName + "Array"
        val putInitializer = "put" + when(valueType) {
                                         Type.STRING    -> "String(k, v)"
                                         Type.DOUBLE    -> "Double(k, v)"
                                         Type.INT       -> "Int(k, v)"
                                         Type.BOOL      -> "Boolean(k, v)"
                                         Type.FLOAT,
                                         Type.LONG      -> "Double(k, v.toDouble())"
                                         Type.ENUM      -> "String(k, v.name)"
                                         else           -> "Map(k, v.${Utils.toMapMethod}())"
                                     }

        return StringBuilder().append("\n\tval ")
                              .append(arrayName)
                              .append(" = ")
                              .append(Utils.createArray)
                              .append("\n\t")
                              .append("for ((k ,v) in ")
                              .append(fieldName)
                              .append("Map) {\n\t\t")
                              .append("val ")
                              .append(element)
                              .append(" = ")
                              .append(Utils.createMap)
                              .append("\n\t\t")
                              .append(element)
                              .append('.')
                              .append(putInitializer)
                              .append("\n\t\t")
                              .append(fieldName)
                              .append("Array.pushMap(item)\n\t}\n\t")
                              .append("putArray(\"")
                              .append(fieldName)
                              .append("\", ")
                              .append(arrayName)
                              .append(')')
                              .toString()
    }

    /**
     * Key type is String always
     */
    class Builder: MessageReactField.Builder() {
        internal var valueType = Type.STRING

        fun valueType(valueType: Type) = apply {
            this.valueType = valueType
        }

        override fun build() : MapReactField {
            return MapReactField(this)
        }
    }
}