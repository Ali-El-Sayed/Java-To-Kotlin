package com.example.javatokotlin.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Repository constructor(
    @PrimaryKey var id: Int = 0,
    var name: String? = null,
    var language: String? = null,

    @SerializedName("html_url")
    var htmlUrl: String? = null,
    var description: String? = null,

    @SerializedName("stargazers_count")
    var stars: Int? = 0,

    @SerializedName("watchers_count")
    var watchers: Int? = 0,
    var forks: Int? = 0,
    var owner: Owner? = null
) : RealmObject()

open class Owner constructor(
    var id: Int = 0,
    var login: String? = null
) : RealmObject()

//public final by default
open class SearchResponse constructor(
    @SerializedName("total_count")
    var totalCount: Int = 0,
    var items: RealmList<Repository>? = null
) : RealmObject()

data class ErrorResponse constructor(val message: String?)