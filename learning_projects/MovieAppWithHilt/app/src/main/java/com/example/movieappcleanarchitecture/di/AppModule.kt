package com.example.movieappcleanarchitecture.di

import android.content.Context
import com.example.movieappcleanarchitecture.data.local.MovieLocalDataSource
import com.example.movieappcleanarchitecture.data.remote.MovieRemoteDataSource
import com.example.movieappcleanarchitecture.data.repository.MovieRepository
import com.example.movieappcleanarchitecture.data.repository.MovieRepositoryImpl
import com.example.movieappcleanarchitecture.domain.usecase.GetPopularMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * DI module — the "recipe book" that tells the DI graph HOW to create each dependency.
 *
 * WHAT IS @Module?
 * - A class annotated with @Module contains methods that create objects.
 * - The DI framework reads this class and learns: "To create a MovieRepository, call provideMovieRepository()"
 *
 * WHAT IS @Provides?
 * - Each method annotated with @Provides creates ONE dependency.
 * - The DI framework calls these methods automatically when something needs that type.
 * - The method parameters are dependencies too — the framework will provide them.
 *
 * HOW IT WORKS:
 *   When someone needs GetPopularMoviesUseCase:
 *   1. The graph sees provideGetPopularMoviesUseCase(repository) — needs a MovieRepository
 *   2. The graph sees provideMovieRepository(remote, local) — needs RemoteDS and LocalDS
 *   3. The graph sees provideMovieRemoteDataSource() — no dependencies, creates it
 *   4. The graph sees provideMovieLocalDataSource(context) — needs Context
 *   5. Hilt provides the application Context via @ApplicationContext
 *   6. Chains everything together automatically
 *
 * THIS IS THE SAME AS AppContainer — but DI automates the wiring:
 *
 *   AppContainer (manual):                    AppModule:
 *   val remoteDS = MovieRemoteDataSource()    @Provides fun provideRemoteDS() = MovieRemoteDataSource()
 *   val localDS = MovieLocalDataSource(ctx)   @Provides fun provideLocalDS(ctx) = MovieLocalDataSource(ctx)
 *   val repo = MovieRepositoryImpl(r, l)      @Provides fun provideRepo(r, l) = MovieRepositoryImpl(r, l)
 *   val useCase = GetPopularMoviesUseCase(r)  @Provides fun provideUseCase(r) = GetPopularMoviesUseCase(r)
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMovieRemoteDataSource(): MovieRemoteDataSource {
        return MovieRemoteDataSource()
    }

    @Provides
    fun provideMovieLocalDataSource(@ApplicationContext context: Context): MovieLocalDataSource {
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
