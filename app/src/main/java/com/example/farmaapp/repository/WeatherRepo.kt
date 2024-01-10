package com.example.farmaapp.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.farmaapp.api.RetroApi
import com.example.farmaapp.model.WeatherModel
import javax.inject.Inject

class WeatherRepo @Inject constructor(
    private val retrofitApi: RetroApi
) {
    private var _weatherData = MutableLiveData<WeatherModel>()
    val weatherData: LiveData<WeatherModel>
        get() = _weatherData

    suspend fun getWeatherData(lat: Double, lon: Double) {
        val result = retrofitApi.getHourlyWeatherData(lat, lon)
        if (result.isSuccessful && result.body() != null) {
            _weatherData.postValue(result.body())
        }
    }

}