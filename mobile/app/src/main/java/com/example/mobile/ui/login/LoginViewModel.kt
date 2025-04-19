package com.example.mobile.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.model.User.User
import com.example.mobile.model.User.UserType
import com.example.mobile.remote.dtos.auth.LoginResponse
import com.example.mobile.remote.dtos.auth.RegisterResponse
import com.example.mobile.repositories.AuthRepository
import com.example.mobile.utils.UiState
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

    private val _loginState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val loginState: StateFlow<UiState<LoginResponse>> = _loginState


    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            val result = authRepository.login(username, password)
            _loginState.value = when {
                result.isSuccess -> {
                    val loginResponse = result.getOrNull()!!
                    User.setUser(
                            personalNo = loginResponse.personalNo,
                            email = loginResponse.email,
                            userType = UserType.fromString(loginResponse.userType),
                            accessToken = loginResponse.accessToken,
                    )
                    UiState.Success(loginResponse)
                }
                else -> UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}