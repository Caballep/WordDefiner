package com.example.nikejosecaballero.network.UrbanDictionary

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UrbanDictionaryAPI {
    @GET("define")
    @Headers(
        UrbanDictionaryUtils.HEADERS.HOST,
        UrbanDictionaryUtils.HEADERS.KEY
    )
    suspend fun getDefinitions(@Query("term") term: String): UrbanDictionaryResponse
}