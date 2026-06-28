package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcleanarchitecture.domain.usecase.GetPopularMoviesUseCase
import com.example.movieappcleanarchitecture.presentation.models.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf<DashboardUiState>(DashboardUiState.Loading)
    val uiState: State<DashboardUiState> = _uiState

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