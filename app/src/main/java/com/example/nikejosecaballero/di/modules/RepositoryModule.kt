package com.example.nikejosecaballero.di.modules

import com.example.nikejosecaballero.repositories.UrbanDictionaryRepo
import com.example.nikejosecaballero.repositories.XYZServicesDummyRepo
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/*
@Module(includes = [NetworkModule::class])
class RepositoryModule {
    @Singleton
    @Provides
    @Named(urbanDictionaryRepo)
    fun provideUrbanDictionaryRepo(): UrbanDictionaryRepo =
        UrbanDictionaryRepo()

    @Singleton
    @Provides
    @Named(xyzServicesDummyRepo)
    fun provideXYZServicesDummyRepo(): XYZServicesDummyRepo =
        XYZServicesDummyRepo()

    companion object {
        const val urbanDictionaryRepo = "urbanDictionaryRepo"
        const val xyzServicesDummyRepo = "xyzServicesDummyRepo"
    }
}
*/