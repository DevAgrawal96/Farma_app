package com.example.farmaapp.api

import com.example.farmaapp.custom.Constants.WEATHER_END_POINT
import com.example.farmaapp.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface RetroApi {
    @GET(WEATHER_END_POINT)
    suspend fun getHourlyWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double
    ): Response<WeatherModel>

    @GET("forecast?latitude=52.52&longitude=13.41&daily=temperature_2m,is_day,relativehumidity_2m&windspeed_unit=ms&forecast_days=1")
    suspend fun getDailyWeatherData(): Response<WeatherModel>
}