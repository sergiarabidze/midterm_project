package com.example.findmovie.data.repositoryimpl

import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun registerUser(
        email: String,
        password: String,
        username: String
    ): Resource<Boolean> {
        return try {

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: return Resource.Error("User registration failed")

            val userMap = hashMapOf(
                "uid" to user.uid,
                "email" to email,
                "name" to username
            )

            firestore.collection("users")
                .document(user.uid)
                .set(userMap).await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error occurred")
        }
    }

    override suspend fun loginUser(email: String, password: String): Resource<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun getCurrentUserName(): Flow<Resource<String>> = flow {
        try {
            val user = auth.currentUser ?: throw Exception("User not logged in")
            val documentSnapshot = firestore.collection("users")
            val uid = user.uid
            val preDoc = documentSnapshot.document(uid)
            val getDoc = preDoc.get()
            val doc = getDoc.await()

            if (doc.exists()) {
                val userName = doc.getString("name") ?: "No name found"
                emit(Resource.Success(userName))
            } else {
                emit(Resource.Error("User document does not exist"))
            }
        } catch (e: Throwable) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error occurred"))
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserEmail() : Flow<Resource<String>> = flow {
        try {
            val email = auth.currentUser?.email
            emit(Resource.Success(email?:"email"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Login failed"))
        }
    }

    override fun logOut() {
        auth.signOut()
    }
}