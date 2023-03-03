package com.example.newsapp.repository

import com.example.newsapp.models.API
import com.example.newsapp.models.NewsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RetrofitRepository {

    //for get the data directly from the service without put it into room database
    suspend fun getDataFromApiRepository(
        apikey: String,
        category: String,
        lang: String,
        country: String,
        max: Int,
        from: String,
        to: String,
        nullable: String
    ) : Flow<NewsData?> {
        return flow {
            emit((API.apiService.getDataFromApiRetrofitService(
                apikey,
                category,
                lang,
                country,
                max,
                from,
                to,
                nullable
            )).body())
        }
    }

}