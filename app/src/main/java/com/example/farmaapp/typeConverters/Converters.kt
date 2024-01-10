package com.example.farmaapp.typeConverters

import androidx.room.TypeConverter
import com.example.farmaapp.model.CurrentWeather
import com.example.farmaapp.model.Hourly
import com.example.farmaapp.model.HourlyUnits
import com.example.farmaapp.model.newsModels.Articles
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CurrentWeatherConverters {
    @TypeConverter
    fun toString(value: CurrentWeather): String {
        val gson = Gson()
        val type = object : TypeToken<CurrentWeather>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCurrentWeather(value: String): CurrentWeather {
        val gson = Gson()
        val type = object : TypeToken<CurrentWeather>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toString(value: Hourly): String {
        val gson = Gson()
        val type = object : TypeToken<Hourly>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toHourly(value: String): Hourly {
        val gson = Gson()
        val type = object : TypeToken<Hourly>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toString(value: HourlyUnits): String {
        val gson = Gson()
        val type = object : TypeToken<HourlyUnits>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toHourlyUnits(value: String): HourlyUnits {
        val gson = Gson()
        val type = object : TypeToken<HourlyUnits>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toString(value: Articles): String {
        val gson = Gson()
        val type = object : TypeToken<Articles>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toArticleList(value: String): Articles {
        val gson = Gson()
        val type = object : TypeToken<Articles>() {}.type
        return gson.fromJson(value, type)
    }
}