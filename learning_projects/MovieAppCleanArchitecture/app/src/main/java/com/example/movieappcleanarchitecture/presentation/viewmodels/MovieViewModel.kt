package com.example.movieappcleanarchitecture.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcleanarchitecture.presentation.models.MovieState
import com.example.movieappcleanarchitecture.services.movieService
import kotlinx.coroutines.launch

// ViewModel takes care the communication between the data and ui
// Prepare everything so that the ui can work with it
class MovieViewModel : ViewModel() {
    // This is the private state variable which is initialized with RecipeState
    // Whenever the _moviesState value changes/updates it will trigger recomposition
    private val _moviesState = mutableStateOf(MovieState())

    // This is the public variable which can be accessed from the outside
    val moviesState: State<MovieState> = _moviesState

    // On object creation we are fetching the categories
    init {
        fetchMovies()
    }

    // viewModelScope provides a scope for suspend function to be processed
    // A suspend function is a function that runs in the background without blocking the main thread
    // To start a suspend function we have to start inside a co-routine
    // Here viewModelScope will create a coroutine scope
    // Basically coroutine will allow you to run a routine in the background
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
                println("fetchMovies error: ${e.cause} - ${e.message}")
                _moviesState.value = _moviesState.value.copy(
                    loading = false,
                    error = "Error fetching categories ${e.cause} - ${e.message}"
                )
            }
        }
    }
}