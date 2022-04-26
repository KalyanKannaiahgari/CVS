package com.example.codingchallenge.repository

import com.example.codingchallenge.model.ImageData
import com.example.codingchallenge.model.Items
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitService {

    @GET("photos_public.gne?format=json&nojsoncallback=1&tags=porcupine")
    fun getAllImages() : Call<ImageData>

    companion object {

        var retrofitService: RetrofitService? = null

        fun getInstance() : RetrofitService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.flickr.com/services/feeds/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}