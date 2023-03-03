package com.example.newsapp.roomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newsapp.models.News


@androidx.room.Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewsData(data: List<News>)

    @Query("SELECT * FROM news_table ORDER BY publishedAt")
    fun readAllNewsData() : LiveData<List<News>>

    //To delete all data in data base:
    @Query("DELETE FROM news_table")
    suspend fun deleteAllNewsDetails()

    //To Update database
    @Update
    suspend fun updateAllNesData(data: List<News>)
}