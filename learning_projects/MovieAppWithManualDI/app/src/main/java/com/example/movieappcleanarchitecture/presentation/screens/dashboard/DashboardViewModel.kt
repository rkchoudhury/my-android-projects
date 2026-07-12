package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movieappcleanarchitecture.domain.usecase.GetPopularMoviesUseCase
import com.example.movieappcleanarchitecture.presentation.models.DashboardUiState
import kotlinx.coroutines.launch

class DashboardViewModel(private val getPopularMovies: GetPopularMoviesUseCase) : ViewModel() {

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

// Factory — tells Android HOW to create the ViewModel with custom parameters
class DashboardViewModelFactory(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(getPopularMoviesUseCase) as T
    }
}