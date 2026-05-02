package com.example.movieappcleanarchitecture.di

import android.content.Context
import com.example.movieappcleanarchitecture.data.local.MovieLocalDataSource
import com.example.movieappcleanarchitecture.data.remote.MovieRemoteDataSource
import com.example.movieappcleanarchitecture.data.repository.MovieRepositoryImpl
import com.example.movieappcleanarchitecture.domain.usecase.GetPopularMoviesUseCase

class AppContainer(context: Context) {

    private val movieRemoteDataSource = MovieRemoteDataSource()
    private val movieLocalDataSource = MovieLocalDataSource(context)
    private val movieRepository = MovieRepositoryImpl(movieRemoteDataSource, movieLocalDataSource)

    val getPopularMoviesUseCase = GetPopularMoviesUseCase(movieRepository)
}