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
        if (protoModel.hasMiddleName()) {
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

For ReactNative protogen will create two extension functions:
```kotlin
fun com.company.project.models.User.toWritableMap(): com.facebook.react.bridge.WritableMap =
      com.facebook.react.bridge.Arguments.createMap().apply {
      	putString("id", id)
      	putString("firstName", firstName)
      	putString("lastName", lastName)
      	if (hasMiddleName())
      	    putBoolean("middleName", middleName)
      	if (contactsList.isNotEmpty()) {
        	val contactsArray = com.facebook.react.bridge.Arguments.createArray()
        	contactsList.forEach { contactsArray.pushString(it) }
        	putArray("contacts", contactsArray)
        }
      }


fun com.company.project.models.User.Builder.fromReadableMap(map: com.facebook.react.bridge.ReadableMap): com.company.project.models.User {
  	id = map.getString("id").toLong()
    firstName = map.getString("firstName")
    lastName = map.getString("lastName")
    if (map.hasKey("middleName")) 
          comment = map.getString("middleName")
    if (map.hasKey("contacts")) {
    	val contactsArray = map.getArray("contacts")
    	for (i in 0 until contactsArray.size()) {
    		val element = com.company.project.models.User.newBuilder().fromReadableMap(contactsArray.getMap(i))
    		addContactsArray(element)
    	}
    }
    return build()
}
```
