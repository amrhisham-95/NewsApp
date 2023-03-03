package com.example.newsapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.newsapp.models.News
import com.example.newsapp.models.NewsData
import com.example.newsapp.repository.RetrofitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RetrofitViewModel : ViewModel(){

    val mutableLiveData = MutableLiveData<NewsData>()


    private val repoRetrofit = RetrofitRepository()
    //for put the data directly from the service without put it into room database
    fun getDataRetrofitViewModel(apikey: String,
                                 category : String,
                                 lang :String,
                                 country :String,
                                 max :Int,
                                 from : String,
                                 to :String,
                                 nullable :String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                repoRetrofit.getDataFromApiRepository(
                    apikey,
                    category,
                    lang,
                    country,
                    max,
                    from,
                    to,
                    nullable
                ).collect {
                    mutableLiveData.postValue(it)
                }
            }
        }

    }
    }