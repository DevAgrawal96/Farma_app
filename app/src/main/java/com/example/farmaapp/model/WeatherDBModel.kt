package com.example.farmaapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherInfo")
data class WeatherDBModel(
    @PrimaryKey @ColumnInfo(name = "weatherId") val weatherId: String,
    val current_weather: CurrentWeather,
    val elevation: Double,
    val generationtime_ms: Double,
    val hourly: Hourly,
    val hourly_units: HourlyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)