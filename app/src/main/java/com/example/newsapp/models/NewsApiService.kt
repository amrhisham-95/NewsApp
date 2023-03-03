package com.example.newsapp.models

import com.example.newsapp.ui.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRetrofitServiceInterface {

    @GET("api/v4/top-headlines")
    suspend fun getDataFromApiRetrofitService(
        @Query("apikey") apikey : String,
        @Query("category") category : String,
        @Query("lang") lang:String,
        @Query("country") country:String,
        @Query("max") max:Int,
        @Query("from") from:String,
        @Query("to") to:String,
        @Query("nullable") nullable:String
    ): Response<NewsData>

}


object API {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
        .build()

    val apiService: ApiRetrofitServiceInterface =
        retrofit.create(ApiRetrofitServiceInterface::class.java)
}
