package com.example.mydemo.di

import com.example.mydemo.business.bands.BandsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private val contentType = "application/json".toMediaType()

    @Provides
    fun bandsService(retrofit: Retrofit): BandsApiService {
        return retrofit.create(BandsApiService::class.java)
    }

    @Provides
    @Singleton
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .client(OkHttpClient().newBuilder().build())
            .addConverterFactory(Json.asConverterFactory(contentType))
            .baseUrl("https://wherever.ch/hslu/rock-bands/")
            .build()
    }
}