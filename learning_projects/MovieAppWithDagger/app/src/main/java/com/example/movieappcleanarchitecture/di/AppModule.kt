package com.example.movieappcleanarchitecture.di

import android.content.Context
import com.example.movieappcleanarchitecture.data.local.MovieLocalDataSource
import com.example.movieappcleanarchitecture.data.remote.MovieRemoteDataSource
import com.example.movieappcleanarchitecture.data.repository.MovieRepository
import com.example.movieappcleanarchitecture.data.repository.MovieRepositoryImpl
import com.example.movieappcleanarchitecture.domain.usecase.GetPopularMoviesUseCase
import dagger.Module
import dagger.Provides

/**
 * Dagger Module — the "recipe book" that tells Dagger HOW to create each dependency.
 *
 * WHAT IS @Module?
 * - A class annotated with @Module contains methods that create objects.
 * - Dagger reads this class and learns: "To create a MovieRepository, I call provideMovieRepository()"
 *
 * WHAT IS @Provides?
 * - Each method annotated with @Provides creates ONE dependency.
 * - Dagger calls these methods automatically when something needs that type.
 * - The method parameters are dependencies too — Dagger will provide them.
 *
 * HOW IT WORKS:
 *   When someone needs GetPopularMoviesUseCase:
 *   1. Dagger sees provideGetPopularMoviesUseCase(repository) — needs a MovieRepository
 *   2. Dagger sees provideMovieRepository(remote, local) — needs RemoteDS and LocalDS
 *   3. Dagger sees provideMovieRemoteDataSource() — no dependencies, creates it
 *   4. Dagger sees provideMovieLocalDataSource(context) — needs Context
 *   5. Dagger gets Context from AppComponent
 *   6. Chains everything together automatically
 *
 * THIS IS THE SAME AS AppContainer — but Dagger automates the wiring:
 *
 *   AppContainer (manual):                    AppModule (Dagger):
 *   val remoteDS = MovieRemoteDataSource()    @Provides fun provideRemoteDS() = MovieRemoteDataSource()
 *   val localDS = MovieLocalDataSource(ctx)   @Provides fun provideLocalDS(ctx) = MovieLocalDataSource(ctx)
 *   val repo = MovieRepositoryImpl(r, l)      @Provides fun provideRepo(r, l) = MovieRepositoryImpl(r, l)
 *   val useCase = GetPopularMoviesUseCase(r)  @Provides fun provideUseCase(r) = GetPopularMoviesUseCase(r)
 */
@Module
class AppModule {

    @Provides
    fun provideMovieRemoteDataSource(): MovieRemoteDataSource {
        return MovieRemoteDataSource()
    }

    @Provides
    fun provideMovieLocalDataSource(context: Context): MovieLocalDataSource {
        return MovieLocalDataSource(context)
    }

    @Provides
    fun provideMovieRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: MovieLocalDataSource
    ): MovieRepository {
        return MovieRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    fun provideGetPopularMoviesUseCase(movieRepository: MovieRepository): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(movieRepository)
    }
}
