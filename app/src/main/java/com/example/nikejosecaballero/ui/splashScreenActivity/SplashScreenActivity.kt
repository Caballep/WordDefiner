package com.example.nikejosecaballero.ui.splashScreenActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.nikejosecaballero.MainApplication
import com.example.nikejosecaballero.R
import com.example.nikejosecaballero.di.modules.ViewModelFactory
import kotlinx.android.synthetic.main.activity_splash_screen.*
import javax.inject.Inject

class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val  splashScreenActivityVM: SplashScreenActivityVM by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SplashScreenActivityVM::class.java]
    }
    private val className = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(className, "onCreate")
        setContentView(R.layout.activity_splash_screen)

        (application as MainApplication).component.inject(this)

        supportActionBar?.hide()

        lifecycle.addObserver(splashScreenLoadingText)

        splashScreenActivityVM.serviceLoadedFlag.observe(this, Observer { servicesLoaded ->
            if(servicesLoaded) {
                splashScreenActivityVM.launchMainActivity(this)
            }
        })
    }
}
