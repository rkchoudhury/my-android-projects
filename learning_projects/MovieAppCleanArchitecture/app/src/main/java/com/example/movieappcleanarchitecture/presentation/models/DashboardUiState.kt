package com.example.movieappcleanarchitecture.presentation.models

import com.example.movieappcleanarchitecture.datatemp.model.Movie

data class DashboardUiState(
    val list: List<Movie> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null,
)