package com.example.findmovie.presentation.features.my_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.data.repository.UserMoviesRepository
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
class MyListViewModel @Inject constructor(
    private val repository: UserMoviesRepository
) : ViewModel() {

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
            repository.getUserMovies().collect { response ->
                _userMoviesState.update { response }
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