package com.example.farmaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.farmaapp.api.NewsApi
import com.example.farmaapp.dao.NewsDao
import com.example.farmaapp.dao.WeatherDao
import com.example.farmaapp.model.NewsDBModel
import com.example.farmaapp.model.WeatherDBModel
import com.example.farmaapp.model.WeatherModel
import com.example.farmaapp.model.newsModels.NewsModel
import com.example.farmaapp.repository.NewsRepository
import com.example.farmaapp.repository.WeatherDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherDao: WeatherDao,
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) :
    ViewModel() {
    private val weatherDBRepository: WeatherDBRepository = WeatherDBRepository(weatherDao)
    private val newsRepository: NewsRepository = NewsRepository(newsApi, newsDao)

    fun addWeatherInfo(weatherDBModel: WeatherDBModel) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherDBRepository.addWeatherInfo(weatherDBModel)
        }
    }

    fun getWeatherInfo(): LiveData<WeatherDBModel> {
        return weatherDBRepository.getWeatherInfo().asLiveData(Dispatchers.Main)
    }

    fun addNewsDataInDB(newsDBModel: NewsDBModel) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.addNewsDataInDB(newsDBModel)
        }
    }

    fun getNewsDataFromDB(): LiveData<NewsDBModel> {
        return newsDao.getNewsData().asLiveData(Dispatchers.Main)
    }

    val newsData: LiveData<NewsModel>
        get() = newsRepository.newsData


    fun getNewsDataFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.getNewsDataFromApi()
        }
    }

}