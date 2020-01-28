package com.example.nikejosecaballero

import android.app.Application
import android.util.Log
import com.example.nikejosecaballero.di.components.AppComponent
import com.example.nikejosecaballero.di.components.DaggerAppComponent
import com.example.nikejosecaballero.di.modules.NetworkModule

open class MainApplication : Application() {
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        createComponent()
    }

    protected open fun createComponent() {
        component = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .build()
    }
}