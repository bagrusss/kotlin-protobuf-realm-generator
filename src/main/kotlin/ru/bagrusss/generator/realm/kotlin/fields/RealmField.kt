package ru.bagrusss.generator.realm.kotlin.fields

import ru.bagrusss.generator.fields.Field

abstract class RealmField<T>(builder: RealmFieldBuilder<T>): Field<T>(builder) {
    protected val primaryKey    = builder.primaryKey
    protected val realmPackage  = builder.realmPackage
}