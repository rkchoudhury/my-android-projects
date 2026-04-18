package com.example.movieappcleanarchitecture.presentation.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.movieappcleanarchitecture.presentation.components.MovieGrid
import com.example.movieappcleanarchitecture.presentation.viewmodels.MovieViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardScreen() {
    val movieViewModel: MovieViewModel = viewModel()
    val movieList by movieViewModel.moviesState

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MovieGrid(movieList)
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}