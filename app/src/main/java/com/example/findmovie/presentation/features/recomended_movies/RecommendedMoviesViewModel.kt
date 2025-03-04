package com.example.findmovie.presentation.features.recomended_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.data.repository.MoviesRepository
import com.example.findmovie.presentation.mapper.mapToMovies
import com.example.findmovie.presentation.model.MoviesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendedMoviesViewModel @Inject constructor(private val repository: MoviesRepository) :ViewModel() {

    private val _userMoviesState = MutableStateFlow<Resource<List<MoviesModel>>>(Resource.Loading)

    private val _selectedGenre = MutableStateFlow<String?>(null)
    val selectedGenre: StateFlow<String?> = _selectedGenre.asStateFlow()

    val filteredMovies = _userMoviesState.combine(_selectedGenre) { resource, genre ->
        when (resource) {
            is Resource.Success -> Resource.Success(filterMovies(resource.data, genre))
            is Resource.Error -> Resource.Error(resource.message)
            is Resource.Loading -> Resource.Loading
            is Resource.Idle -> Resource.Idle
        }
    }

    init {
        fetchUserMovies()
    }

    private fun fetchUserMovies() {
        viewModelScope.launch {
            repository.getRecommendedMovies().collect { response ->
                when (response) {
                    is Resource.Error -> {
                        _userMoviesState.update { Resource.Error(response.message) }
                    }
                    is Resource.Idle -> {
                        _userMoviesState.update { Resource.Idle }
                    }
                    is Resource.Loading -> {
                        _userMoviesState.update { Resource.Loading }
                    }
                    is Resource.Success ->  _userMoviesState.update { Resource.Success(response.data.map { it.mapToMovies() }) }
                }
            }
        }
    }

    fun selectGenre(genre: String?) {
        _selectedGenre.value = if (_selectedGenre.value == genre) {
            null
        } else {
            genre
        }
    }

    private fun filterMovies(
        movies: List<MoviesModel>,
        selectedGenre: String?
    ): List<MoviesModel> {
        return when (selectedGenre) {
            null -> movies
            "All" -> movies
            else -> movies.filter { it.genres.contains(selectedGenre) }
        }
    }
}