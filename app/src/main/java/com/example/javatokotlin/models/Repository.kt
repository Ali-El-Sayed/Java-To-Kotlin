package com.example.javatokotlin.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Repository constructor(
    @PrimaryKey
    var id: Int,
    var name: String?,
    var language: String?,

    @SerializedName("html_url")
    var htmlUrl: String?,
    var description: String?,

    @SerializedName("stargazers_count")
    var stars: Int?,

    @SerializedName("watchers_count")
    var watchers: Int?,
    var forks: Int?,
    var owner: Owner?
)

