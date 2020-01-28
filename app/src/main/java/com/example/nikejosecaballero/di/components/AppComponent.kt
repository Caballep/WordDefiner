package com.example.nikejosecaballero.di.components

import com.example.nikejosecaballero.di.modules.DummyModule
import com.example.nikejosecaballero.di.modules.NetworkModule
import com.example.nikejosecaballero.di.modules.ViewModelModule
import com.example.nikejosecaballero.ui.mainActivity.MainActivity
import com.example.nikejosecaballero.ui.splashScreenActivity.SplashScreenActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class,
    DummyModule::class,
    ViewModelModule::class
])
interface AppComponent {
    fun inject(activity: SplashScreenActivity)
    fun inject(activity: MainActivity)
}