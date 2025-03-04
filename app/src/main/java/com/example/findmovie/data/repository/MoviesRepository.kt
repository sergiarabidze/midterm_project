package com.example.findmovie.data.repository

import com.example.findmovie.data.remote.dtoClasses.MoviesDto
import com.example.findmovie.data.remote.httpRequest.Resource
import kotlinx.coroutines.flow.Flow


interface MoviesRepository {
    suspend fun getRecommendedMovies(): Flow<Resource<List<MoviesDto>>>

    suspend fun getPopularMovies(): Flow<Resource<List<MoviesDto>>>
}