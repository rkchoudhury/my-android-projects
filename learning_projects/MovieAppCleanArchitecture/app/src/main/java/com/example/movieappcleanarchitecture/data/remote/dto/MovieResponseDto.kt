package com.example.movieappcleanarchitecture.data.remote.dto

import com.example.movieappcleanarchitecture.data.Movie

data class MovieDtoResponse(
    val results: List<Movie>
)