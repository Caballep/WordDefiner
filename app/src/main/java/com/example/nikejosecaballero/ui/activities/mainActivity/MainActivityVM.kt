package com.example.nikejosecaballero.ui.activities.mainActivity

import android.util.Log
import androidx.lifecycle.*
import com.example.nikejosecaballero.network.UrbanDictionary.Definition
import com.example.nikejosecaballero.repositories.UrbanDictionaryRepo
import com.example.nikejosecaballero.utils.DefinitionsSortType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject

class MainActivityVM @Inject constructor(
    private var urbanDictionaryRepo: UrbanDictionaryRepo
) : ViewModel() {

    private val className = this.javaClass.simpleName
    private var unsortedDefinitions: List<Definition>? = null
    var definitions = MutableLiveData<List<Definition>>()

    fun requestDefinitions(word: String?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                definitions.postValue(urbanDictionaryRepo.getDefinitions(word))
            }
        }
    }

    fun sortDefinitions(sortType: DefinitionsSortType) {
        Log.i(className, "sortDefinitions by $sortType")
        if (unsortedDefinitions == null) {
            unsortedDefinitions = definitions.value
        }
        when (sortType) {
            DefinitionsSortType.HIGHEST_RATE -> {
                definitions.value = definitions.value?.sortedByDescending { it.thumbsUp }
            }
            DefinitionsSortType.LOWEST_RATE -> {
                definitions.value = definitions.value?.sortedByDescending { it.thumbsDown }
            }
            else -> {
                definitions.value = unsortedDefinitions
            }
        }
    }

    @TestOnly
    fun _setUrbanDictionaryRepo(urbanDictionaryRepo: UrbanDictionaryRepo) {
        this.urbanDictionaryRepo = urbanDictionaryRepo
    }
}