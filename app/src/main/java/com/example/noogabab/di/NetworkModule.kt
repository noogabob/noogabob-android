package com.example.noogabab.di

import com.example.noogabab.data.api.ApiService
import com.example.noogabab.util.NetworkConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    @Provides
    fun provideBaseUrl(): String = NetworkConstants.BASE_URL

    @Provides
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideOkHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(logger)
        okHttpClient.callTimeout(2000, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(2000, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(2000, TimeUnit.SECONDS)
        okHttpClient.readTimeout(2000, TimeUnit.SECONDS)
        return okHttpClient.build()
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
        baseUrl: String,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    fun provideTimelineService(retrofit: Retrofit): ApiService =
        retrofit!!.create(ApiService::class.java)
}