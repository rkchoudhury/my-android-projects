package com.example.cleanarchitecture.framework.di

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(val app: Application) {

    @Provides
    fun provideApp(): Application {
        return app
    }
}