package com.example.movieappcleanarchitecture.di

import android.content.Context
import com.example.movieappcleanarchitecture.domain.usecase.GetPopularMoviesUseCase
import dagger.BindsInstance
import dagger.Component

/**
 * Dagger Component — the "bridge" that connects the Module (recipes) to the app.
 *
 * WHAT IS @Component?
 * - An interface that Dagger reads to generate a class called "DaggerAppComponent".
 * - You define WHAT dependencies are available. Dagger generates HOW to deliver them.
 * - modules = [AppModule::class] tells Dagger: "Use AppModule's @Provides methods"
 *
 * WHAT IS @Component.Factory?
 * - A way to pass external dependencies (like Context) into the Dagger graph.
 * - Context comes from Android OS — Dagger can't create it, so we pass it in.
 * - @BindsInstance tells Dagger: "Store this Context and use it whenever someone needs Context"
 *
 * WHAT DOES getPopularMoviesUseCase() DO?
 * - It's an "exposure" method — tells Dagger what the outside world can request.
 * - When you call component.getPopularMoviesUseCase(), Dagger:
 *   1. Creates MovieRemoteDataSource (from AppModule)
 *   2. Creates MovieLocalDataSource with Context (from AppModule)
 *   3. Creates MovieRepositoryImpl with both (from AppModule)
 *   4. Creates GetPopularMoviesUseCase with Repository (from AppModule)
 *   5. Returns it to you
 *
 * AFTER BUILD, Dagger auto-generates:
 *   DaggerAppComponent — a class that implements this interface
 *   You use it as: DaggerAppComponent.factory().create(context)
 */
@Component(modules = [AppModule::class])
interface AppComponent {

    fun getPopularMoviesUseCase(): GetPopularMoviesUseCase

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
