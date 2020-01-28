package com.example.nikejosecaballero.di.modules

import android.content.Context
import com.example.nikejosecaballero.BuildConfig
import com.example.nikejosecaballero.network.UrbanDictionary.UrbanDictionaryAPI
import com.example.nikejosecaballero.network.UrbanDictionary.UrbanDictionaryUtils
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.time.Duration
import javax.inject.Singleton

@Module
class NetworkModule(private val applicationContext: Context) {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val cacheSize = (5 * 1024 * 1024)
        val timeOutMillis = 5000
        val cache = Cache(
            File(applicationContext.cacheDir, "http"),
            cacheSize.toLong()
        )
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(Duration.ofMillis(timeOutMillis.toLong()))
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
            .baseUrl(UrbanDictionaryUtils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideUrbanDictionaryAPIService(retrofit: Retrofit): UrbanDictionaryAPI =
        retrofit.create(UrbanDictionaryAPI::class.java)
}