package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieappcleanarchitecture.presentation.components.LoadingIndicator
import com.example.movieappcleanarchitecture.presentation.components.MovieError
import com.example.movieappcleanarchitecture.presentation.components.MovieGrid
import com.example.movieappcleanarchitecture.presentation.models.DashboardUiState

@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = hiltViewModel()
    val uiState by viewModel.uiState

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = uiState) {
            is DashboardUiState.Loading -> {
                LoadingIndicator()
            }

            is DashboardUiState.Error -> {
                MovieError("Something went wrong\n" + state.message)
            }

            is DashboardUiState.Success -> {
                MovieGrid(state.movies)
            }
        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}