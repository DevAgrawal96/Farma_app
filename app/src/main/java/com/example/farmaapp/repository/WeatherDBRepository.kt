package com.example.farmaapp.repository

import com.example.farmaapp.dao.WeatherDao
import com.example.farmaapp.model.NewsDBModel
import com.example.farmaapp.model.WeatherDBModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherDBRepository @Inject constructor(private val weatherDao: WeatherDao) {

    fun addWeatherInfo(weatherDBModel: WeatherDBModel) {
        weatherDao.addWeatherData(weatherDBModel)
    }

    fun getWeatherInfo(): Flow<WeatherDBModel> {
        return weatherDao.getWeatherData()
    }


}