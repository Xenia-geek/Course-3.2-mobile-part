package com.example.courseproject.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NetworkService {
    private static final String BASE_URL = "http://10.0.2.2:55090/api/";

    public static JSONPlaceHolderApi getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JSONPlaceHolderApi apiJSON = retrofit.create(JSONPlaceHolderApi.class);
        return apiJSON;

    }
}

