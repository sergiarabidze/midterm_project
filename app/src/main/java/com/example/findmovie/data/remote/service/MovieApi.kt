package com.example.findmovie.data.remote.service

import com.example.findmovie.data.remote.dtoClasses.MoviesDto
import retrofit2.Response
import retrofit2.http.GET

interface MovieApi {
    @GET("/movies")
    suspend fun getRecommendedMovies(): Response<List<MoviesDto>>

    @GET("/popularmovies")
    suspend fun getPopularMovies(): Response<List<MoviesDto>>
}
