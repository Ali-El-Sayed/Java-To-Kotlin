package com.example.javatokotlin.retrofit

import retrofit2.Retrofit
import com.example.javatokotlin.retrofit.GithubAPIService
import com.example.javatokotlin.retrofit.RetrofitClient
import retrofit2.converter.gson.GsonConverterFactory

//Object declarations are initialized lazily when accessed
object RetrofitClient {
    private const val BASE_URL = "https://api.github.com/"

    // object declaration is initialized lazy by default
    private val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val githubAPIService: GithubAPIService by lazy {
        retrofit.create(GithubAPIService::class.java)

    }

}