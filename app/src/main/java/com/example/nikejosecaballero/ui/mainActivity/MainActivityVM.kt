package com.example.nikejosecaballero.ui.mainActivity

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.nikejosecaballero.R
import com.example.nikejosecaballero.network.UrbanDictionary.Definition
import com.example.nikejosecaballero.repositories.UrbanDictionaryRepo
import javax.inject.Inject

class MainActivityVM @Inject constructor(
    private val urbanDictionaryRepo: UrbanDictionaryRepo
) : ViewModel() {

    private val className = this.javaClass.simpleName
    private var liveDefinitions: MutableLiveData<List<Definition>>? = null
    private var unsortedDefinitions: List<Definition>? = null

    fun sortDefinitions(sortType: String, context: Context) { // TODO: Ideally sortType should be an Enum
        val sortOptions: Array<String> =
            context.resources.getStringArray(R.array.sort_options)
        when (sortType) {
            sortOptions[1] -> {
                liveDefinitions?.value = unsortedDefinitions?.sortedBy { it.thumbsUp }
            }
            sortOptions[2] -> {
                liveDefinitions?.value = unsortedDefinitions?.sortedBy { it.thumbsDown }
            }
            else -> liveDefinitions?.value = unsortedDefinitions
        }
    }

    fun getDefinitions(word: String): LiveData<List<Definition>> {
        liveDefinitions = urbanDictionaryRepo.getDefinitions(word)
        unsortedDefinitions = liveDefinitions?.value
        return liveDefinitions as LiveData<List<Definition>>
    }

    override fun onCleared() {
        super.onCleared()
        urbanDictionaryRepo.cancelJobs()
        Log.i(className, "onCleared")
    }
}