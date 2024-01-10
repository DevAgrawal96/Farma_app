package com.example.farmaapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.farmaapp.model.NewsDBModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewsData(newsDBModel: NewsDBModel)

    @Query("select * from newsData")
    fun getNewsData(): Flow<NewsDBModel>
}