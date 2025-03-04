package com.example.findmovie.presentation.features.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<Resource<Boolean>>(Resource.Idle)
    val registerState: StateFlow<Resource<Boolean>> get() = _registerState.asStateFlow()

    fun register(email: String, password: String, username: String) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading
            val result = authRepository.registerUser(email, password, username)
            _registerState.value = result
        }
    }

    fun isUserLoggedIn() : Boolean {
        return authRepository.isUserLoggedIn()
    }
}

