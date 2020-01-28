package com.example.nikejosecaballero.di.modules

import android.content.Context
import com.example.nikejosecaballero.MainApplication
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {
    @Provides
    fun provideApplicationContext(application: MainApplication): Context = application.applicationContext
}