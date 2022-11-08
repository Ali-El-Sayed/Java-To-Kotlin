package com.example.javatokotlin.retrofit

import com.example.javatokotlin.models.Repository
import retrofit2.http.GET
import com.example.javatokotlin.models.SearchResponse
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.QueryMap

// public abstract by default
interface GithubAPIService {
    
    // https://api.github.com/search/repositories?q=mario+language=java
    @GET("search/repositories")
    fun searchRepositories(@QueryMap options: Map<String?, String?>?): Call<SearchResponse?>?

    // https://api.github.com/users/{ali-el-sayed}/repos
    @GET("/users/{username}/repos")
    fun searchRepositoriesByUser(@Path("username") githubUser: String?): Call<List<Repository?>?>?
}