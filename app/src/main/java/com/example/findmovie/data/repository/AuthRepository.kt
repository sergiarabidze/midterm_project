package com.example.findmovie.data.repository

import com.example.findmovie.data.remote.httpRequest.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerUser(email: String, password: String, username: String): Resource<Boolean>
    suspend fun loginUser(email: String, password: String): Resource<Boolean>
    suspend fun getCurrentUserName(): Flow<Resource<String>>
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserEmail(): Flow<Resource<String>>
    fun logOut()
}
