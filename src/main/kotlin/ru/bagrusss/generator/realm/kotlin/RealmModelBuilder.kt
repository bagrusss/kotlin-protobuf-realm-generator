package ru.bagrusss.generator.realm.kotlin

import ru.bagrusss.generator.fields.Field
import ru.bagrusss.generator.model.ModelBuilder
import ru.bagrusss.generator.realm.kotlin.fields.LinkingObjectsRealmField
import java.util.*

abstract class RealmModelBuilder: ModelBuilder() {

    internal var realmPackageName = ""
    internal var realmClassName = ""
    internal val linkedObjects: LinkedList<Field<LinkingObjectsRealmField>> = LinkedList()

    fun realmPackageName(realmPackageName: String) = apply {
        this.realmPackageName = realmPackageName
    }

    fun realmClassName(realmClassName: String) = apply {
        this.realmClassName = realmClassName
    }


    fun addLinkedObject(linkedObject: Field<LinkingObjectsRealmField>) = apply {
        linkedObjects.add(linkedObject)
    }


}