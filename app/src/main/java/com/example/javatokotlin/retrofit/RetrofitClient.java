package com.example.javatokotlin.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    private static final String BASE_URL = "https://api.github.com/";
    private static Retrofit retrofit = null;

    public static GithubAPIService getGithubAPIService() {
        return getClient().create(GithubAPIService.class);
    }

    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
