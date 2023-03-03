package com.example.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsData(
    var articles : List<News>,
)

@Entity(tableName = "news_table")
data class News(
    var title : String,
    var description : String,
    var content : String,
    var image : String,
    var url : String,
    @PrimaryKey
    var publishedAt : String,
)



