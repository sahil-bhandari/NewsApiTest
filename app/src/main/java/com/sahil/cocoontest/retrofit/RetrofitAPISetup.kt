package com.sahil.cocoontest.retrofit

import com.sahil.cocoontest.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPISetup {

    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    }


    val API_INTERFACE: APIInterface by lazy{
        retrofitBuilder
            .build()
            .create(APIInterface::class.java)
    }

}

