package com.example.farmaapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.farmaapp.api.NewsApi
import com.example.farmaapp.dao.NewsDao
import com.example.farmaapp.model.NewsDBModel
import com.example.farmaapp.model.newsModels.NewsModel
import com.example.farmaapp.utils.log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApi,private val newsDao: NewsDao) {

    private var _newsdata = MutableLiveData<NewsModel>()
    val newsData: LiveData<NewsModel> get() = _newsdata

    suspend fun getNewsDataFromApi() {
        val result = newsApi.getNewsData()
        log("newsData",result.body().toString())
        if (result.isSuccessful && result.body() != null) {
            _newsdata.postValue(result.body())
        }
    }

    fun addNewsDataInDB(newsDBModel: NewsDBModel) {
        newsDao.addNewsData(newsDBModel)
    }

    fun getNewsDataFromDB(): Flow<NewsDBModel> {
        return newsDao.getNewsData()
    }
}