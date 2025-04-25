package com.example.mobile.ui.accountant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobile.remote.dtos.auth.DepartmentBudgetResponse
import com.example.mobile.remote.dtos.auth.DepartmentBudgetResponseList
import com.example.mobile.repositories.DepartmentRepository
import com.example.mobile.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepartmentViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository,
): ViewModel() {

    companion object {
        const val TAG = "DepartmentViewModel"
    }
    private val _departmentBudgetState = MutableStateFlow<UiState<List<DepartmentBudgetResponse>>>(UiState.Idle)
    val departmentBudgetState: StateFlow<UiState<List<DepartmentBudgetResponse>>> = _departmentBudgetState

    // Add any necessary properties and methods for the ViewModel here
    // For example, you might want to add LiveData or StateFlow properties to hold data
    // and methods to fetch or update that data.

    fun getDepartmentsWithBudget() {
        viewModelScope.launch {
            _departmentBudgetState.value = UiState.Loading
            val result = departmentRepository.getDepartmentBudget()
            if (result.isSuccess) {
                Log.i("DepartmentViewModel", "Success: ${result.getOrNull()}")
                _departmentBudgetState.value = UiState.Success(result.getOrNull()!!)
            } else {
                Log.i("DepartmentViewModel", "Error: ${result.exceptionOrNull()?.message}")
                _departmentBudgetState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun setInitialBudget(deptName:String, newInitialBudget:Double){
        viewModelScope.launch {
            _departmentBudgetState.value = UiState.Loading
            val result = departmentRepository.setInitialBudget(deptName, newInitialBudget)
            if (result.isSuccess) {
                Log.i("DepartmentViewModel", "Success: ${result.getOrNull()}")
                //_departmentBudgetState.value = UiState.Success(result.getOrNull()!!)
            } else {
                Log.i("DepartmentViewModel", "Error: ${result.exceptionOrNull()?.message}")
                //_departmentBudgetState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun setRemainingBudget(department: String, budgetDouble: Double) {
        viewModelScope.launch {
            _departmentBudgetState.value = UiState.Loading
            val result = departmentRepository.setRemainingBudget(department, budgetDouble)
            if (result.isSuccess) {
                Log.i("DepartmentViewModel", "Success: ${result.getOrNull()}")
                //_departmentBudgetState.value = UiState.Success(result.getOrNull()!!)
            } else {
                Log.i("DepartmentViewModel", "Error: ${result.exceptionOrNull()?.message}")
                //_departmentBudgetState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }

    }
    fun resetRemainingBudget(department: String) {
        viewModelScope.launch {
            _departmentBudgetState.value = UiState.Loading
            val result = departmentRepository.resetRemainingBudget(department)
            if (result.isSuccess) {
                Log.i("DepartmentViewModel", "Success: ${result.getOrNull()}")
                //_departmentBudgetState.value = UiState.Success(result.getOrNull()!!)
            } else {
                Log.i("DepartmentViewModel", "Error: ${result.exceptionOrNull()?.message}")
                //_departmentBudgetState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }

    }
}