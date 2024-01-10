package com.example.farmaapp.modules

import com.example.farmaapp.api.NewsApi
import com.example.farmaapp.api.RetroApi
import com.example.farmaapp.custom.Constants.BASE_URL
import com.example.farmaapp.custom.Constants.NEWS_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun providesWeatherAPI(retrofitBuilder: Retrofit.Builder): RetroApi {
        return retrofitBuilder.baseUrl(BASE_URL)
            .build().create(RetroApi::class.java)
    }

    @Singleton
    @Provides
    fun providesNewsAPI(retrofitBuilder: Retrofit.Builder): NewsApi {
        return retrofitBuilder.baseUrl(NEWS_BASE_URL)
            .build().create(NewsApi::class.java)
    }
}