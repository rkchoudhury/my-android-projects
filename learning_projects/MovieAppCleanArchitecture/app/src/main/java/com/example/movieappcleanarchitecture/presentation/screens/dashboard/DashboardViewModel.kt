package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcleanarchitecture.data.local.MovieLocalDataSource
import com.example.movieappcleanarchitecture.data.remote.MovieRemoteDataSource
import com.example.movieappcleanarchitecture.data.repository.MovieRepositoryImpl
import com.example.movieappcleanarchitecture.domain.usecase.GetPopularMoviesUseCase
import com.example.movieappcleanarchitecture.presentation.models.DashboardUiState
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = mutableStateOf<DashboardUiState>(DashboardUiState.Loading)
    val uiState: State<DashboardUiState> = _uiState

    private val movieRepository =
        MovieRepositoryImpl(
            MovieRemoteDataSource(),
            MovieLocalDataSource(application.applicationContext)
        )
    private val getPopularMovies = GetPopularMoviesUseCase(movieRepository)

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                val movies = getPopularMovies()
                _uiState.value = DashboardUiState.Success(movies)
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }
}