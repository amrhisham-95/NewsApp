package com.example.newsapp.repository

import androidx.lifecycle.LiveData
import com.example.newsapp.models.API
import com.example.newsapp.models.News
import com.example.newsapp.roomDatabase.Dao

class RoomRepository(private val dao : Dao) {

    val readAllNewsDataRepo : LiveData<List<News>> = dao.readAllNewsData()

    suspend fun addNewsDataRepo(data : List<News>){
        dao.addNewsData(data)
    }

    //suspend fun To delete all news :
    suspend fun deleteAllNewsDetails() {
        dao.deleteAllNewsDetails()
    }

    //update data
    suspend fun updateAllNewsData(data : List<News>){
        dao.updateAllNesData(data)
    }


    }

