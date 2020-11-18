package com.sahil.cocoontest.retrofit

import com.sahil.cocoontest.models.network.Base
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {


    @GET("home.json")
    fun  getTopStories(
        @Query("api-key") apiKey: String
    ): retrofit2.Call<Base>
}