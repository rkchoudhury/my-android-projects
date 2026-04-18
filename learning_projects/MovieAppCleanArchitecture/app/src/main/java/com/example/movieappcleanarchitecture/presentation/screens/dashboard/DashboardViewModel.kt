package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcleanarchitecture.presentation.models.MovieState
import com.example.movieappcleanarchitecture.services.movieService
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    // This is the private state variable, whenever the _moviesState value changes/updates it will trigger recomposition
    private val _moviesState = mutableStateOf(MovieState())

    // This is the public variable which can be accessed from the outside
    val moviesState: State<MovieState> = _moviesState

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                val response = movieService.getPopularMovies()
                _moviesState.value = _moviesState.value.copy(
                    list = response.results,
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