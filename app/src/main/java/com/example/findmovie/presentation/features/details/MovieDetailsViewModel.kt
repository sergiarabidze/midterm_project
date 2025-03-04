package com.example.findmovie.presentation.features.details

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.data.repository.UserMoviesRepository
import com.example.findmovie.presentation.model.MoviesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel
@Inject constructor(
    private val repository: UserMoviesRepository
) : ViewModel() {

    private val _userMoviesState = MutableStateFlow(true)
     val userMoviesState: StateFlow<Boolean> get() = _userMoviesState


    private val _addMovieState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val addMovieState: StateFlow<Resource<Unit>> get() = _addMovieState

    private val _removeMovieState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val removeMovieState: StateFlow<Resource<Unit>> get() = _removeMovieState

    fun addMovie(movie: MoviesModel) {
        viewModelScope.launch {
            repository.addMovie(movie).collect { resource ->
                _addMovieState.value = resource
            }
        }
    }

    fun removeMovie(movie: MoviesModel) {
        viewModelScope.launch {
            repository.removeMovie(movie).collect { resource ->
                _removeMovieState.value = resource
            }
        }
    }

    fun isMovieLiked(movieId :String){
        viewModelScope.launch {
            repository.getMovieIds().collect{
                when(it){
                    is Resource.Success -> {
                        val movieIds = it.data
                        _userMoviesState.update {  movieIds.contains(movieId)  }
                    }
                    else -> _userMoviesState.update { false }
                }
            }
        }
    }
}
