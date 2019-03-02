# Code generator for protobuf

* Generates `RealmObject` classes that creates from Proto models and transforms into Proto models
* Generates Kotlin extension functions than puts Proto models into ReactNative `WritableMap` and creates Proto model from `ReadableMap` 

Kotlin support only now.

Example proto model:

```proto
syntax = "proto2";

package models;

option java_package = "com.company.project.models";


message User {
    required int64 id = 1;
    required string firstName = 2;
    required string lastName = 3;
    optional string middleName = 4;
    
    repeated User contacts = 5;
}

```

Protogen will create Realm model:
```kotlin
open class RealmUser: RealmObject {

    var id: Long = 0
    
    var firstName: String = ""
    
    var lastName: String = ""
    
    var middleName: String? = null
    
    var contacts: RealmList<com.company.project.models.RealmUser>? = null
    
    constructor()
    
    constructor(protoModel: com.company.project.models.User) {
        id = protoModel.id
        firstName = protoModel.firstName
        lastName = protoModel.lastName
        if (protoModel.hasMiddleName) {
            middleName = protoModel.middleName
        }
        if (protoModel.contactsCount > 0) {
            contacts = io.realm.RealmList()
            contacts!!.addAll(protoModel.contactsList.map { com.company.project.models.RealmUser(it) })
        }
    }
    
    fun toProto(): com.company.project.models.User {
        val p = com.company.project.models.User.newBuilder()
        p.id = id
        p.firstName = firstName
        p.lastName = lastName
        middleName?.let { p.middleName = it }
        contacts?.let { p.addAllContacts(it.map { it.toProto() }) }
        return p.build()
    }
}
```

