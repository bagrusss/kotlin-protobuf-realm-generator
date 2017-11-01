package ru.bagrusss.generator.realm.kotlin.fields

import ru.bagrusss.generator.fields.Field

abstract class RealmField<T>(builder: RealmFieldBuilder<T>) : Field<T>(builder) {

    protected val primaryKey            = builder.primaryKey
    protected val realmPackage          = builder.realmPackage
    protected val indexed               = builder.indexed

    protected val protoConstructorParameter = "protoModel"

    protected val primaryKeyAnnotation      = "io.realm.annotations.PrimaryKey"
    protected val indexAnnotation           = "io.realm.annotations.Index"
    protected val linkedObjectAnnotation    = "io.realm.annotations.LinkingObjects"

    protected val realmResultsClass         = "io.realm.RealmResults"

    lateinit var toProtoInitializer: String
    lateinit var fromProtoInitializer: String
}