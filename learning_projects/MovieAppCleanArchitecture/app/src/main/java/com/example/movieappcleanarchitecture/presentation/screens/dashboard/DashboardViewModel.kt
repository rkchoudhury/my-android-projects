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
    // This is the private state variable, whenever the _moviesState value changes/updates it will trigger recomposition
    private val _moviesState = mutableStateOf(DashboardUiState())

    // This is the public variable which can be accessed from the outside
    val moviesState: State<DashboardUiState> = _moviesState

    val movieRepository = MovieRepositoryImpl(MovieRemoteDataSource())
    val getPopularMovies = GetPopularMoviesUseCase(movieRepository)

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                val response = getPopularMovies()
                _moviesState.value = _moviesState.value.copy(
                    list = response,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _moviesState.value = _moviesState.value.copy(
                    loading = false,
                    error = "Error fetching movies ${e.cause} - ${e.message}"
                )
            }
        }
    }
}