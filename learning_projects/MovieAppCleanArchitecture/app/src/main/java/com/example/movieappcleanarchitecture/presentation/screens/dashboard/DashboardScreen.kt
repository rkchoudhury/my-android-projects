package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieappcleanarchitecture.presentation.components.LoadingIndicator
import com.example.movieappcleanarchitecture.presentation.components.MovieGrid
import com.example.movieappcleanarchitecture.presentation.viewmodels.MovieViewModel

@Composable
fun DashboardScreen() {
    val movieViewModel: MovieViewModel = viewModel()
    val movieList by movieViewModel.moviesState

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when {
            movieList.loading -> {
                LoadingIndicator()
            }

            else -> {
                MovieGrid(movieList.list)
            }
        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}