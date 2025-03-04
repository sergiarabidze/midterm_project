package com.example.findmovie.presentation.features.home_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.data.repository.AuthRepository
import com.example.findmovie.data.repository.MoviesRepository
import com.example.findmovie.data.repository.UserMoviesRepository
import com.example.findmovie.presentation.mapper.mapToMovies
import com.example.findmovie.presentation.model.MoviesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val moviesRepository: MoviesRepository,
    private val userMoviesRepository: UserMoviesRepository
) : ViewModel() {


    private val _userNameState = MutableStateFlow<Resource<String>>(Resource.Loading)
    val userNameState: StateFlow<Resource<String>> get() = _userNameState

    private val _userMoviesState = MutableStateFlow<Resource<List<MoviesModel>>>(Resource.Loading)
    val userMoviesState: StateFlow<Resource<List<MoviesModel>>> get() = _userMoviesState

    private val _popularMoviesState =
        MutableStateFlow<Resource<List<MoviesModel>>>(Resource.Loading)
    val popularMoviesState: StateFlow<Resource<List<MoviesModel>>> get() = _popularMoviesState


    private val _recommendedMoviesState =
        MutableStateFlow<Resource<List<MoviesModel>>>(Resource.Loading)
    val recommendedMoviesState: StateFlow<Resource<List<MoviesModel>>> get() = _recommendedMoviesState

    init {
        fetchUserName()
        fetchPopularMovies()
        fetchRecommendedMovies()
    }

    private fun fetchUserName() {
        viewModelScope.launch {
            authRepository.getCurrentUserName().collect {
                _userNameState.value = it
            }
        }
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch {
            moviesRepository.getPopularMovies().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _popularMoviesState.update {
                            Resource.Success(response.data.map { dto ->
                                dto.mapToMovies()
                            })
                        }
                    }

                    is Resource.Error -> {
                        _popularMoviesState.update {
                            Resource.Error(response.message)
                        }
                    }


                    Resource.Idle -> {
                        _popularMoviesState.update {
                            Resource.Idle
                        }
                    }

                    Resource.Loading -> {
                        _popularMoviesState.update {
                            Resource.Loading
                        }
                    }
                }
            }
        }
    }

    private fun fetchRecommendedMovies() {
        viewModelScope.launch {
            moviesRepository.getRecommendedMovies().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _recommendedMoviesState.update {
                            Resource.Success(response.data.map { dto ->
                                dto.mapToMovies()
                            })
                        }
                    }

                    is Resource.Error -> {
                        _recommendedMoviesState.update {
                            Resource.Error(response.message)
                        }
                    }

                    is Resource.Loading -> {
                        _recommendedMoviesState.update {
                            Resource.Loading
                        }
                    }

                    is Resource.Idle -> {
                        _recommendedMoviesState.update {
                            Resource.Idle
                        }
                    }
                }
            }
        }
    }

    fun fetchUserMovies() {
        viewModelScope.launch {
            userMoviesRepository.getUserMovies().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _userMoviesState.update {
                            Resource.Success(response.data)
                        }
                    }

                    is Resource.Error -> {
                        _userMoviesState.update {
                            Resource.Error(response.message)
                        }
                    }

                    is Resource.Loading -> {
                        _userMoviesState.update {
                            Resource.Loading
                        }
                    }

                    is Resource.Idle -> {
                        _userMoviesState.update {
                            Resource.Idle
                        }
                    }
                }
            }
        }
    }
}

