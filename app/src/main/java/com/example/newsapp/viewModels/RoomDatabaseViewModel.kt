package com.example.newsapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.News
import com.example.newsapp.repository.RoomRepository
import com.example.newsapp.roomDatabase.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomDatabaseViewModel(application: Application) : AndroidViewModel(application) {

     val readAllNewsDataViewModel : LiveData<List<News>>
     private val repoRoom : RoomRepository


    init {
        val daoViewModel = RoomDatabase.getDatabase(application).daoDatabase()
        repoRoom = RoomRepository(daoViewModel)
        readAllNewsDataViewModel = repoRoom.readAllNewsDataRepo
    }


    fun addNewsDataViewModel(data:List<News>){
        viewModelScope.launch(Dispatchers.IO) {
            repoRoom.addNewsDataRepo(data)
        }
    }

    //Fun to delete all news:
    fun deleteAllNewsDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            repoRoom.deleteAllNewsDetails()
        }
    }

    fun updateDataViewModel(data:List<News>){
        viewModelScope.launch(Dispatchers.IO) {
            repoRoom.updateAllNewsData(data)
        }
    }
}