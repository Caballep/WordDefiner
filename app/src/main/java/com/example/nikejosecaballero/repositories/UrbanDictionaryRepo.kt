package com.example.nikejosecaballero.repositories

import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nikejosecaballero.MainApplication
import com.example.nikejosecaballero.di.modules.NetworkModule
import com.example.nikejosecaballero.network.UrbanDictionary.Definition
import com.example.nikejosecaballero.network.UrbanDictionary.UrbanDictionaryAPI
import com.example.nikejosecaballero.network.UrbanDictionary.UrbanDictionaryResponse
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

//class UserRepository @Inject constructor(private val api: GithubServices) {
class UrbanDictionaryRepo @Inject constructor(
    private val urbanDictionaryAPIClient: UrbanDictionaryAPI
) {
    private val className = this.javaClass.simpleName

    private var job: CompletableJob? = null

    private var urbanDictionaryResponseLiveData: MutableLiveData<List<Definition>>? = null

    fun getDefinitions(word: String): MutableLiveData<List<Definition>> {
        job = Job()

        urbanDictionaryResponseLiveData = object: MutableLiveData<List<Definition>>(){
            override fun onActive() {
                super.onActive()
                job?.let{
                    val coroutineContext = Dispatchers.IO + it
                    CoroutineScope(coroutineContext).launch {
                        delay(1000) // TODO: REMOVE THIS, Faking a network delay so the Loading element is more noticeable
                        Log.i(className, "Executing urbanDictionaryAPI.getDefinitions at coroutine context $coroutineContext")
                        val response = urbanDictionaryAPIClient.getDefinitions(word).list
                        withContext(Dispatchers.Main){
                            value = response
                            it.complete()
                        }
                    }
                }
            }
        }
        return urbanDictionaryResponseLiveData as MutableLiveData<List<Definition>>
    }

    fun cancelJobs(){
        job?.cancel()
    }
}