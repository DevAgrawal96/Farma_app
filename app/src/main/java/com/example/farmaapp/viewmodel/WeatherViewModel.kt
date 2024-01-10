package com.example.farmaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmaapp.model.WeatherModel
import com.example.farmaapp.repository.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repositories: WeatherRepo) : ViewModel() {
    val weatherData: LiveData<WeatherModel>
        get() = repositories.weatherData

    fun getWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            repositories.getWeatherData(lat, lon)
        }
    }
}