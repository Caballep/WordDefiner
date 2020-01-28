package com.example.nikejosecaballero.di.components

import com.example.nikejosecaballero.di.modules.ApplicationModule
import com.example.nikejosecaballero.di.modules.NetworkModule
import com.example.nikejosecaballero.di.modules.ViewModelModule
import com.example.nikejosecaballero.ui.activities.mainActivity.MainActivity
import com.example.nikejosecaballero.ui.activities.splashScreenActivity.SplashScreenActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    NetworkModule::class,
    ViewModelModule::class
])
interface AppComponent {
    fun inject(activity: SplashScreenActivity)
    fun inject(activity: MainActivity)
}