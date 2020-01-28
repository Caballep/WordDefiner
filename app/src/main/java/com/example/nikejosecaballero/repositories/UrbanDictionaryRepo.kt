package com.example.nikejosecaballero.repositories

import android.util.Log
import com.example.nikejosecaballero.network.UrbanDictionary.Definition
import com.example.nikejosecaballero.network.UrbanDictionary.UrbanDictionaryAPI
import javax.inject.Inject

class UrbanDictionaryRepo @Inject constructor(
    private val urbanDictionaryAPIClient: UrbanDictionaryAPI
) {
    private val className = this.javaClass.simpleName

    suspend fun getDefinitions(word: String?): List<Definition> {
        Log.i(className, "getDefinitions for: '${word}'")
        return urbanDictionaryAPIClient.getDefinitions(word).list
    }
}