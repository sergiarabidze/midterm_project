package com.example.findmovie.data.repositoryimpl

import com.example.findmovie.data.remote.httpRequest.ApiHelper
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.data.repository.UserMoviesRepository
import com.example.findmovie.presentation.mapper.toFirestoreMap
import com.example.findmovie.presentation.mapper.toMoviesModel
import com.example.findmovie.presentation.model.MoviesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserMoviesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val helper: ApiHelper
) : UserMoviesRepository {

    override suspend fun addMovie(movie: MoviesModel): Flow<Resource<Unit>> {
        val currentUserId = auth.currentUser?.uid
            ?: return flow {
                emit(Resource.Error("User not authenticated"))
            }
        return helper.safeFireBaseCall {
            firestore.collection("users")
                .document(currentUserId)
                .update("movies", FieldValue.arrayUnion(movie.toFirestoreMap()))
            firestore.collection("users").document(currentUserId)
                .update("moviesId", FieldValue.arrayUnion(movie.id))
        }.map { resource ->
            when (resource) {
                is Resource.Success -> Resource.Success(Unit)
                is Resource.Error -> Resource.Error(resource.message)
                is Resource.Loading -> Resource.Loading
                is Resource.Idle -> Resource.Idle
            }
        }
    }


    override suspend fun removeMovie(movie: MoviesModel): Flow<Resource<Unit>> {
        val currentUserId = auth.currentUser?.uid
            ?: return flow {
                emit(Resource.Error("User not authenticated"))
            }
        return helper.safeFireBaseCall {
            firestore.collection("users")
                .document(currentUserId)
                .update("movies", FieldValue.arrayRemove(movie.toFirestoreMap()))
            firestore.collection("users").document(currentUserId)
                .update("moviesId", FieldValue.arrayRemove(movie.id))
        }.map { resource ->
            when (resource) {
                is Resource.Success -> Resource.Success(Unit)
                is Resource.Error -> Resource.Error(resource.message)
                is Resource.Loading -> Resource.Loading
                is Resource.Idle -> Resource.Idle
            }
        }
    }


    override suspend fun getUserMovies(): Flow<Resource<List<MoviesModel>>> {
        val currentUserId = auth.currentUser?.uid
            ?: return flow {
                emit(Resource.Error("User not authenticated"))
            }
        return helper.safeFireBaseCall {
            firestore.collection("users")
                .document(currentUserId)
                .get()
        }.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val moviesList =
                        resource.data["movies"] as? List<Map<String, Any>> ?: emptyList()
                    Resource.Success(moviesList.map { it.toMoviesModel() })
                }
                is Resource.Error -> {
                    Resource.Error(resource.message)
                }

                is Resource.Loading -> {
                    Resource.Loading
                }

                is Resource.Idle -> {
                    Resource.Idle
                }
            }
        }
    }


    override suspend fun getMovieIds(): Flow<Resource<List<String>>> {
        val currentUserId = auth.currentUser?.uid
            ?: return flow {
                emit(Resource.Error("User not authenticated"))
            }
        return helper.safeFireBaseCall {
            firestore.collection("users")
                .document(currentUserId)
                .get()
        }.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val movieIds = resource.data["moviesId"] as? List<String> ?: emptyList()
                    Resource.Success(movieIds)
                }

                is Resource.Error -> {
                    Resource.Error(resource.message)
                }

                is Resource.Loading -> {
                    Resource.Loading
                }

                is Resource.Idle -> {
                    Resource.Idle
                }
            }
        }
    }
}
