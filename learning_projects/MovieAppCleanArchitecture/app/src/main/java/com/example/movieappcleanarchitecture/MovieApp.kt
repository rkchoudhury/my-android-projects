package com.example.movieappcleanarchitecture

import android.app.Application
import com.example.movieappcleanarchitecture.di.AppContainer

class MovieApp : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}