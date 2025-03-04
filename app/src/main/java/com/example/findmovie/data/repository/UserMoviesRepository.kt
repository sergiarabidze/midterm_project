package com.example.findmovie.data.repository

import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.presentation.model.MoviesModel
import kotlinx.coroutines.flow.Flow

interface UserMoviesRepository {
    suspend fun addMovie(movie: MoviesModel): Flow<Resource<Unit>>
    suspend fun removeMovie(movie: MoviesModel): Flow<Resource<Unit>>
    suspend fun getUserMovies(): Flow<Resource<List<MoviesModel>>>
    suspend fun getMovieIds(): Flow<Resource<List<String>>>
}
