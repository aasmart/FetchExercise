package com.example.fetchexercise.di

import android.app.Application
import com.example.fetchexercise.api.FetchAPI
import com.example.fetchexercise.api.FetchRepository
import com.example.fetchexercise.api.FetchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideApi(app: Application): FetchAPI {
        return Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideFetchRepository(api: FetchAPI): FetchRepository {
        return FetchRepositoryImpl(fetchAPI = api)
    }
}