package com.example.mobile.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.model.User.User
import com.example.mobile.model.User.UserType
import com.example.mobile.remote.dtos.Login.LoginResponse
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


    private val _loginState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val loginState: StateFlow<UiState<LoginResponse>> = _loginState

    companion object {
        const val TAG = "LoginViewModel"
    }
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
                    Log.i(TAG, "Login successful for user: ${User.personalNo}")

                    UiState.Success(loginResponse)
                }

                else -> {
                    Log.i(TAG, "Login failed, invalid credantials")
                    UiState.Error("Login failed, invalid credantials")
                }
            }
        }
    }
}