package com.example.farmaapp.api

import androidx.room.Dao
import com.example.farmaapp.custom.Constants.NEWS_END_POINT
import com.example.farmaapp.model.newsModels.NewsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsApi {
    @GET(NEWS_END_POINT)
    suspend fun getNewsData(): Response<NewsModel>
}