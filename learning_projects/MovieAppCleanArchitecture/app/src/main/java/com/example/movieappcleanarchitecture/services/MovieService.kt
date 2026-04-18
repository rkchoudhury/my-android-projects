package com.example.movieappcleanarchitecture.services

import com.example.movieappcleanarchitecture.data.MovieResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val BASE_URL = "https://api.themoviedb.org/3/movie/"

private const val TOKEN =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ZGFkMTIwMDRjMDdhMGQ4MjI0ZTk0ZGJmNDY4ZWJiZiIsInN1YiI6IjY1ZDk4YTZhOWQ4OTM5MDE2MmRhNzIyOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.36uJTAsCQUrvQw9SCgoVU3L8TqPRJRihp6xnAcgg6tg"

private val retrofit =
    Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

val movieService: MovieService = retrofit.create(MovieService::class.java)

interface MovieService {
    @Headers(
        "Accept: application/json",
        "Authorization: Bearer $TOKEN",
    )
    @GET("popular?language=en-US&page=1")
    suspend fun getPopularMovies(): MovieResponse
}