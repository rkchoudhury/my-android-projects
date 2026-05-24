package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcleanarchitecture.di.AppModule
import com.example.movieappcleanarchitecture.di.DaggerAppComponent
import com.example.movieappcleanarchitecture.domain.usecase.GetPopularMoviesUseCase
import com.example.movieappcleanarchitecture.presentation.models.DashboardUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = mutableStateOf<DashboardUiState>(DashboardUiState.Loading)
    val uiState: State<DashboardUiState> = _uiState

    // Dependency Injection
    @Inject
    lateinit var getPopularMovies: GetPopularMoviesUseCase

    init {
        DaggerAppComponent.builder()
            .appModule(AppModule(getApplication())).build().inject(this)
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