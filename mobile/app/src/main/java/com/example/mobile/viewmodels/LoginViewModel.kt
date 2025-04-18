package com.example.mobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.model.User.UserType
import com.example.mobile.remote.dtos.auth.LoginResponse
import com.example.mobile.remote.dtos.auth.RegisterResponse
import com.example.mobile.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val loginResponse: LoginResponse) : LoginState()
        data class Error(val message: String) : LoginState()
    }
    //TODO fix, is new State class required for every type of request?
    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        data class Success(val registerResponse: RegisterResponse) : RegisterState()
        data class Error(val message: String) : RegisterState()
    }


    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = authRepository.login(username, password)
            _loginState.value = when {
                result.isSuccess -> LoginState.Success(result.getOrNull()!!)
                else -> LoginState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun register(username: String, password: String, personalNo: String, userType: UserType) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val result = authRepository.register(username, password, personalNo, userType)
            _registerState.value = when {
                result.isSuccess -> RegisterState.Success(result.getOrNull()!!)
                else -> RegisterState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}