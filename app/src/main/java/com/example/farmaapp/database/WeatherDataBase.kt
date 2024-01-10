package com.example.farmaapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.farmaapp.dao.WeatherDao
import com.example.farmaapp.model.NewsDBModel
import com.example.farmaapp.model.WeatherDBModel
import com.example.farmaapp.typeConverters.CurrentWeatherConverters

@Database(entities = [WeatherDBModel::class], version = 1, exportSchema = false)
@TypeConverters(CurrentWeatherConverters::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
}