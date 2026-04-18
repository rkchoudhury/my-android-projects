package com.example.movieappcleanarchitecture.data.remote.api.impl

import com.example.movieappcleanarchitecture.data.remote.api.MovieApiService

val movieService: MovieApiService = retrofit.create(MovieApiService::class.java)