package com.example.findmovie.presentation.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.data.repository.MoviesRepository
import com.example.findmovie.presentation.mapper.mapToMovies
import com.example.findmovie.presentation.model.MoviesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _recommendedMoviesState = MutableStateFlow<Resource<List<MoviesModel>>>(Resource.Loading)
    private val _searchResults = MutableStateFlow<Resource<List<MoviesModel>>>(Resource.Loading)
    val searchResults: StateFlow<Resource<List<MoviesModel>>> = _searchResults

    init {
        fetchRecommendedMovies()
    }

    private fun fetchRecommendedMovies() {
        viewModelScope.launch {
            repository.getRecommendedMovies().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        val movies = response.data.map { dto -> dto.mapToMovies() }
                        _recommendedMoviesState.value = Resource.Success(movies)
                        _searchResults.value = Resource.Success(movies) // Initialize search results with all movies
                    }
                    is Resource.Error -> {
                        _recommendedMoviesState.value = Resource.Error(response.message)
                        _searchResults.value = Resource.Error(response.message)
                    }
                    is Resource.Loading -> {
                        _recommendedMoviesState.value = Resource.Loading
                        _searchResults.value = Resource.Loading
                    }
                    is Resource.Idle -> {
                        _recommendedMoviesState.value = Resource.Idle
                        _searchResults.value = Resource.Idle
                    }
                }
            }
        }
    }

    fun searchMovies(query: String) {
        val currentMovies = _recommendedMoviesState.value
        if (currentMovies is Resource.Success) {
            val filteredMovies = currentMovies.data.filter { movie ->
                movie.title.contains(query, ignoreCase = true) ||
                        movie.genres.any { genre -> genre.contains(query, ignoreCase = true) }
            }
            _searchResults.value = Resource.Success(filteredMovies)
        } else {
            _searchResults.value = Resource.Error("No movies available to search")
        }
    }

}