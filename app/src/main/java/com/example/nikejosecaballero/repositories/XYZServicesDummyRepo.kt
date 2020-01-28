package com.example.nikejosecaballero.repositories

import android.util.Log
import com.example.nikejosecaballero.network.UrbanDictionary.UrbanDictionaryAPI
import dagger.Provides
import kotlinx.coroutines.delay
import javax.inject.Inject

class XYZServicesDummyRepo @Inject constructor(private val nothing: String){
    private val className = this.javaClass.simpleName

    init {
        Log.i(className, "Init, dependency injection just for fun: $nothing")
    }

    suspend fun authorizeDevice() {
        delay(4000)
        Log.i(className, "authorizeDevice Done")
    }

    suspend fun launchFirebaseServices() {
        delay(5000)
        Log.i(className, "launchFirebaseServices Done")
    }

    suspend fun launchEssentialsServices() {
        delay(3500)
        Log.i(className, "launchEssentialsServices Done")
    }
}
