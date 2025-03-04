package com.example.findmovie.data.repositoryimpl

import com.example.findmovie.data.remote.dtoClasses.MoviesDto
import com.example.findmovie.data.remote.httpRequest.ApiHelper
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.data.remote.service.MovieApi
import com.example.findmovie.data.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MoviesRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val apiHelper: ApiHelper
) : MoviesRepository {
    override suspend fun getRecommendedMovies(): Flow<Resource<List<MoviesDto>>> {
        return apiHelper.handleHttpRequest {
            api.getRecommendedMovies()
        }
    }

    override suspend fun getPopularMovies(): Flow<Resource<List<MoviesDto>>> {
        return apiHelper.handleHttpRequest {
            api.getPopularMovies()
        }
    }

}