package com.example.farmaapp.di

import android.content.Context
import androidx.room.Room
import com.example.farmaapp.custom.Constants.NEWS_DATABASE_NAME
import com.example.farmaapp.custom.Constants.WEATHER_DATABASE_NAME
import com.example.farmaapp.dao.NewsDao
import com.example.farmaapp.dao.WeatherDao
import com.example.farmaapp.database.NewsDataBase
import com.example.farmaapp.database.WeatherDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun getWeatherDataBaseProvider(@ApplicationContext appContext: Context): WeatherDataBase {
        return Room.databaseBuilder(appContext, WeatherDataBase::class.java, WEATHER_DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun getWeatherDao(weatherDataBase: WeatherDataBase): WeatherDao {
        return weatherDataBase.getWeatherDao()
    }

    @Provides
    @Singleton
    fun getNewsDataBaseProvider(@ApplicationContext appContext: Context): NewsDataBase {
        return Room.databaseBuilder(appContext, NewsDataBase::class.java, NEWS_DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun getNewsDao(newsDataBase: NewsDataBase): NewsDao {
        return newsDataBase.getNewsDao()
    }
}