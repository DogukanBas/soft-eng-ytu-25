package com.example.mobile.ui.notification

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.remote.dtos.Notification.NotificationResponse
import com.example.mobile.repositories.NotificationRepository
import com.example.mobile.ui.admin.AdminViewModel
import com.example.mobile.ui.admin.AdminViewModel.Companion
import com.example.mobile.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _notificationState = MutableStateFlow<UiState<List<NotificationResponse>>>(UiState.Idle)
    val notificationState :StateFlow<UiState<List<NotificationResponse>>> = _notificationState
    companion object {
        const val TAG = "NotificationViewModel"
    }
    fun getUserNotification() {
        viewModelScope.launch {
            _notificationState.value = UiState.Loading
            Log.i(AdminViewModel.TAG, "Set state to Loading")
            try {
                val result = notificationRepository.getUserNotification()
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    Log.i(AdminViewModel.TAG, "Success: $response")
                    _notificationState.value = UiState.Success(response!!)
                } else {
                    val exception = result.exceptionOrNull()
                    val errorMessage = exception?.message ?: "Unknown error"
                    Log.i(AdminViewModel.TAG, "Error: $errorMessage")
                    _notificationState.value = UiState.Error(errorMessage)
                }
            } catch (e: Exception) {
                Log.e(AdminViewModel.TAG, "Unexpected exception in addDepartment", e)
                _notificationState.value = UiState.Error("Unexpected error: ${e.message}")
            }
        }
    }

}