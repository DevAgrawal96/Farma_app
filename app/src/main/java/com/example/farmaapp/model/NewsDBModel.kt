package com.example.farmaapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.farmaapp.model.newsModels.Articles

@Entity(tableName = "newsData")
data class NewsDBModel(
    @PrimaryKey @ColumnInfo(name = "weatherId") val weatherId: String,
    val articles: Articles
)
