package ru.bagrusss.generator.react.kotlin.field

import ru.bagrusss.generator.Utils
import ru.bagrusss.generator.fields.Type

class MapReactField private constructor(builder: Builder): MessageReactField(builder) {

    private val valueType = builder.valueType


    /* to Map
    val requisitesObjectMap = com.facebook.react.bridge.Arguments.createMap()
    for ((k ,v) in requisitesMap) { requisitesObjectMap.putString(k, v) }
    putMap("requisites", requisitesObjectMap)
    */

    /* from Map
    val formObjectsMap = map.getMap("form")
    val iterator = formObjectsMap.keySetIterator()
    do {
    	iterator.nextKey()?.let { formMap.put(it, formObjectsMap.getString(it)) }
    } while(iterator.hasNextKey())
    */

    override fun fromMapInitializer(): String {
        val container = fieldName + "ObjectsMap"
        val initializer = when (valueType) {
            Type.STRING    -> "$container.getString(it)"
            Type.DOUBLE    -> "$container.getDouble(it)"
            Type.INT       -> "$container.getInt(it)"
            Type.BOOL      -> "$container.getBoolean(it)"
            Type.FLOAT     -> "$container.getDouble(it).toFloat()"
            Type.LONG      -> "$container.getDouble(it).toLong()"
            Type.ENUM      -> "$protoFullTypeName.valueOf($container.getString(it))"
            else           -> "$protoFullTypeName.newBuilder().${Utils.fromMapMethod}($container.getMap(it))"
        }
        return StringBuilder().append("\n\tval ")
                              .append(container)
                              .append(" = map.getMap(\"")
                              .append(fieldName)
                              .append("\")\n\t")
                              .append("val iterator = ")
                              .append(container)
                              .append(".keySetIterator()\n\t")
                              .append("do {\n\t\t")
                              .append("iterator.nextKey()?.let { ")
                              .append("put")
                              .append(fieldName[0].toUpperCase())
                              .append(fieldName.substring(1))
                              .append("(it, ")
                              .append(initializer)
                              .append(") }\n\t} while(")
                              .append("iterator.hasNextKey())\n")
                              .toString()
    }

    override fun toMapInitializer(): String {
        val container = fieldName + "ObjectMap"
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
                              .append(container)
                              .append(" = ")
                              .append(Utils.createMap)
                              .append("\n\t")
                              .append("for ((k ,v) in ")
                              .append(fieldName)
                              .append("Map) { ")
                              .append(container)
                              .append('.')
                              .append(putInitializer)
                              .append(" }\n\t")
                              .append("putMap(\"")
                              .append(fieldName)
                              .append("\", ")
                              .append(container)
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