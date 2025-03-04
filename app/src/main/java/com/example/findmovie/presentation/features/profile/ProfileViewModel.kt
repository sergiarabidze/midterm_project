package com.example.findmovie.presentation.features.profile



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmovie.data.remote.httpRequest.Resource
import com.example.findmovie.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authRepository: AuthRepository)  : ViewModel() {
    private val _username = MutableStateFlow<Resource<String>>(Resource.Idle)
    val username: StateFlow<Resource<String>> = _username

    private val _email = MutableStateFlow<Resource<String>>(Resource.Idle)
    val email: StateFlow<Resource<String>> = _email

    init {
        fetchUserName()
        fetchUserEmail()
    }

    private fun fetchUserName() {
        viewModelScope.launch {
            authRepository.getCurrentUserName().collect {
                _username.value = it
            }
        }
    }

    private fun fetchUserEmail() {
        viewModelScope.launch {
            authRepository.getCurrentUserEmail().collect {
                _email.value = it
            }
        }
    }

    fun logOut(){
        authRepository.logOut()
    }
}
