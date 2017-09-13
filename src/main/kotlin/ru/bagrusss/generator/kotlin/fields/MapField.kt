package ru.bagrusss.generator.kotlin.fields

import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.fields.FieldBuilder

class MapField private constructor(builder: Builder) : KotlinField<MapField>(builder) {

    class Builder : FieldBuilder<MapField>() {

        override fun repeated(isRepeated: Boolean): FieldBuilder<MapField> {
            return super.repeated(true)
        }

        override fun build(): Field<MapField> {
            initializer("$realmPackage.$protoPackage$typePrefix$fullProtoTypeName()")
            return MapField(this)
        }

    }

    override fun getFieldType() = protoFullTypeName

    override fun isPrimitive() = false

    override fun repeatedToProtoFill(): String {
        return StringBuilder().append("it.forEach { ")
                              .append("p.put")
                              .append(fieldName[0].toUpperCase() + fieldName.substring(1))
                              .append("(it.key, it.value) }")
                              .toString()
    }

    override fun repeatedFromProtoFill(): String {
        return StringBuilder().append(".")
                              .append(fieldName)
                              .append("Count > 0) {\n")
                              .append(fieldName)
                              .append(" = RealmList()\n")
                              .append("for ((k, v) in protoModel.")
                              .append(fieldName)
                              .append("Map) {\n")
                              .append("\tval e = ")
                              .append(initializerArgs)
                              .append('\n')
                              .append("\te.key = k\n")
                              .append("\te.value = v\n")
                              .append('\t')
                              .append(fieldName)
                              .append("!!.add(e)\n}\n}")
                              .toString()
    }

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()
    }
}