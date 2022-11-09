package com.example.javatokotlin.models

import io.realm.RealmObject

data class Owner constructor(
    var id: Int,
    var login: String?
)