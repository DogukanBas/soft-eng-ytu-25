package com.example.mobile.ui.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.model.User.Employee
import com.example.mobile.remote.dtos.auth.AddUserResponse
import com.example.mobile.repositories.AdminRepository
import com.example.mobile.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(private val adminRepository: AdminRepository) : ViewModel() {
    private val _addUserState = MutableStateFlow<UiState<AddUserResponse>>(UiState.Idle)
    val addUserState: StateFlow<UiState<AddUserResponse>> = _addUserState

    private val _addDepartmentState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val addDepartmentState: StateFlow<UiState<Unit>> = _addDepartmentState

    private val _getDepartmentsState = MutableStateFlow<UiState<List<String>>>(UiState.Idle)
    val getDepartmentsState: StateFlow<UiState<List<String>>> = _getDepartmentsState
    companion object {
        const val TAG = "AdminViewModel"
    }
    fun setGetDepartmentsState(state: UiState<List<String>>) {
        _getDepartmentsState.value = state
    }

    fun getDepartments() {
        Log.i(TAG, "Initialized in AdminViewModel")
        viewModelScope.launch {
            _getDepartmentsState.value = UiState.Loading
            Log.i(TAG, "Set state to Loading")

            try {
                val result = adminRepository.getDepartments()

                if (result.isSuccess) {
                    val response = result.getOrNull()
                    Log.i(TAG, "Success: $response")
                    _getDepartmentsState.value = UiState.Success(response!!)
                } else {
                    val exception = result.exceptionOrNull()
                    val errorMessage = exception?.message ?: "Unknown error"
                    Log.i(TAG, "Error: $errorMessage")

                    _getDepartmentsState.value = UiState.Error(errorMessage)
                    Log.i(TAG,"Current state is: ${_getDepartmentsState.value}")
                }
            } catch (e: Exception) {
                // Catch any unexpected exceptions
                Log.e(TAG, "Unexpected exception in getDepartments", e)
                _getDepartmentsState.value = UiState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    fun addDepartment(departmentName: String) {
        Log.i(TAG, "Initialized in AdminViewModel")
        viewModelScope.launch {
            _addDepartmentState.value = UiState.Loading
            Log.i(TAG, "Set state to Loading")

            try {
                val result = adminRepository.addDepartment(departmentName)

                if (result.isSuccess) {
                    val response = result.getOrNull()
                    Log.i(TAG, "Success: $response")
                    _addDepartmentState.value = UiState.Success(Unit)
                } else {
                    val exception = result.exceptionOrNull()
                    val errorMessage = exception?.message ?: "Unknown error"
                    Log.i(TAG, "Error: $errorMessage")

                    _addDepartmentState.value = UiState.Error(errorMessage)
                    Log.i(TAG,"Current state is: ${_addDepartmentState.value}")
                }
            } catch (e: Exception) {
                // Catch any unexpected exceptions
                Log.e(TAG, "Unexpected exception in addDepartment", e)
                _addDepartmentState.value = UiState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    fun addUser(employee: Employee) {
        Log.i(TAG, "Initialized in AdminViewModel")
        viewModelScope.launch {
            _addUserState.value = UiState.Loading
            Log.i(TAG, "Set state to Loading")

            try {
                val result = adminRepository.addUser(employee)

                if (result.isSuccess) {
                    val response = result.getOrNull()
                    Log.i(TAG, "Success: $response")
                    _addUserState.value = UiState.Success(response!!)
                } else {
                    val exception = result.exceptionOrNull()
                    val errorMessage = exception?.message ?: "Unknown error"
                    Log.i(TAG, "Error: $errorMessage")

                    _addUserState.value = UiState.Error(errorMessage)
                    Log.i(TAG,"Current state is: ${_addUserState.value}")
                }
            } catch (e: Exception) {
                // Catch any unexpected exceptions
                Log.e(TAG, "Unexpected exception in addUser", e)
                _addUserState.value = UiState.Error("Unexpected error: ${e.message}")
            }
        }
    }

}