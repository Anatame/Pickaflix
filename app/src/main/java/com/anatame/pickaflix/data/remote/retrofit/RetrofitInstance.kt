package com.anatame.pickaflix.data.remote.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: VidDataApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://localhost/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VidDataApi::class.java)
    }
}


