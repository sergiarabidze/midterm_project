package com.example.findmovie.data.remote.httpRequest

import android.util.Log.d
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ApiHelper{
    suspend fun <T> handleHttpRequest ( apiCall : suspend () -> Response<T>): Flow<Resource<T>>  = flow{
        try{
            emit(Resource.Loading)
            val response = apiCall.invoke()
            if (response.isSuccessful){
                response.body()?.let {
                   emit(Resource.Success(data = it))
                }?: emit(Resource.Error("Something went wrong"))
            }else{
                emit(Resource.Error(response.code().toString()))
            }
        }catch (io : IOException){
            emit(Resource.Error("Network Failure"))
        }catch (http : HttpException){
            emit(Resource.Error(http.code().toString()))
        }catch (e : Throwable){
            emit(Resource.Error("Something went wrong"))
        }
    }

    fun <T : Any> safeFireBaseCall(call: () -> Task<T>): Flow<Resource<T>> = flow {
        emit(Resource.Loading)

        try {
            val task = call()
            val result = task.await()

            if (task.isSuccessful) {
                emit(Resource.Success(result))
                d("success","$result")
            } else {
                emit(Resource.Error(task.exception?.localizedMessage ?: ""))
                d("error","${task.exception?.localizedMessage}")
            }

        } catch (e: FirebaseAuthException) {
            emit(Resource.Error( e.message ?: ""))
            d("error","${e.message}")
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: ""))
            d("error","${e.message}")
        }
    }

    }

