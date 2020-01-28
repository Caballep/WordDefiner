package com.example.nikejosecaballero.di.modules

import dagger.Module
import dagger.Provides

@Module
class DummyModule {

    @Provides
    fun dummyProvidesNothing(): String = "So we don't only have one module"
}