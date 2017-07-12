package bagrusss.generator.fields

import com.squareup.kotlinpoet.PropertySpec

/**
 * Created by bagrusss on 12.07.17
 */
abstract class Field<T>(builder: FieldBuilder<T>) {
    protected val nullable             = builder.optional
    protected val repeated             = builder.repeated
    protected val typePrefix           = builder.typePrefix
    protected val typeSuffix           = builder.typeSuffix
    protected val fullProtoTypeName    = builder.fullProtoTypeName
    protected val fieldName            = builder.fieldName
    protected val initializerFormat    = builder.initializerFormat
    protected val initializerArgs      = builder.initializerArgs
    protected val generateToProto      = builder.generateToProto
    protected val generateFromProto    = builder.generateFromProto
    protected val primaryKey           = builder.primaryKey

    protected val propertySpec by lazy { getPropSpec() }
    protected val kotlinFieldType by lazy { getFieldType() }

    protected abstract fun getPropSpec(): PropertySpec
    protected abstract fun getFieldType(): String

    lateinit var toProtoInitializer: String
    lateinit var fromProtoInitialized: String
}