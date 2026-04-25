package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcleanarchitecture.datatemp.remote.MovieRemoteDataSource
import com.example.movieappcleanarchitecture.datatemp.repository.MovieRepositoryImpl
import com.example.movieappcleanarchitecture.domaintemp.usecase.GetPopularMoviesUseCase
import com.example.movieappcleanarchitecture.presentation.models.DashboardUiState
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _uiState = mutableStateOf<DashboardUiState>(DashboardUiState.Loading)
    val uiState: State<DashboardUiState> = _uiState

    private val movieRepository = MovieRepositoryImpl(MovieRemoteDataSource())
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