package com.example.farmaapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.farmaapp.model.WeatherDBModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeatherData(weatherDBModel: WeatherDBModel)

    @Query("select * from weatherInfo")
    fun getWeatherData(): Flow<WeatherDBModel>


}