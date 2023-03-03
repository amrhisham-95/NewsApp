package com.example.newsapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.newsapp.models.News

class MyDiffUtil(
    private val oldNews : List<News>,
    private val newNews : List<News>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldNews.size
    }
    override fun getNewListSize(): Int {
        return newNews.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNews[oldItemPosition].url == newNews[newItemPosition].url
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldNews[oldItemPosition].title != newNews[newItemPosition].title -> { false }
            oldNews[oldItemPosition].description != newNews[newItemPosition].description -> { false }
            oldNews[oldItemPosition].content != newNews[newItemPosition].content -> { false }
            oldNews[oldItemPosition].image != newNews[newItemPosition].image -> { false }
            oldNews[oldItemPosition].url != newNews[newItemPosition].url -> { false }
            oldNews[oldItemPosition].publishedAt != newNews[newItemPosition].publishedAt -> { false }
            else -> true
        }
    }
}