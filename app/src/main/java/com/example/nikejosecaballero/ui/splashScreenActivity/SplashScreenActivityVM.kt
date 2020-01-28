package com.example.nikejosecaballero.ui.splashScreenActivity

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nikejosecaballero.repositories.XYZServicesDummyRepo
import com.example.nikejosecaballero.ui.mainActivity.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

class SplashScreenActivityVM @Inject constructor(xyzServicesDummyRepo: XYZServicesDummyRepo) : ViewModel() {

    val serviceLoadedFlag: MutableLiveData<Boolean> = MutableLiveData()

    private val className = this.javaClass.simpleName
    private var jobs: List<Job>? = null

    init {
        Log.i(className, "Initializing application services")

        // This will make the launched services to run in parallel and not sequentially
        CoroutineScope(Main).launch {
            jobs = arrayListOf(
                launch { xyzServicesDummyRepo.authorizeDevice() },
                launch { xyzServicesDummyRepo.launchFirebaseServices() },
                launch { xyzServicesDummyRepo.launchEssentialsServices() }
            )
            jobs?.forEach { it.join() }
            loadingCompleted()
        }
    }

    fun launchMainActivity(activity: Activity) {
        Log.i(className, "Launching MainActivity")
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    private fun loadingCompleted() {
        // TODO: This only handles the happy path
        serviceLoadedFlag.value = true
        Log.i(className, "All services are loaded, serviceLoadedFlag emitted.")
    }

    private fun clearJobs() {
        jobs?.forEach {
            it.cancel()
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearJobs()
        Log.i(className, "onCleared")
    }
}