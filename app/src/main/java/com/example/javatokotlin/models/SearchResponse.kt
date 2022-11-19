package com.example.javatokotlin.models

import io.realm.RealmObject
import com.google.gson.annotations.SerializedName
import io.realm.RealmList

//public final by default
data class SearchResponse constructor(
    @SerializedName("total_count")
    var totalCount: Int, var items: RealmList<Repository>?
) 