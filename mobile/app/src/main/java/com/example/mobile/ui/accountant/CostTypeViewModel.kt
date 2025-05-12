package com.example.mobile.ui.accountant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.remote.dtos.Accountant.CostTypeBudgetResponse
import com.example.mobile.repositories.CostTypeRepository
import com.example.mobile.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CostTypeViewModel @Inject constructor(
    private val costTypeRepository: CostTypeRepository
) : ViewModel() {

    private val _costTypeBudgetState = MutableStateFlow<UiState<List<CostTypeBudgetResponse>>>(UiState.Idle)
    val costTypeBudgetState: StateFlow<UiState<List<CostTypeBudgetResponse>>> = _costTypeBudgetState

    private val _addCostTypeBudgetState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val addCostTypeBudgetState: StateFlow<UiState<Unit>> = _addCostTypeBudgetState


    fun getCostTypesWithBudget() {
        viewModelScope.launch {
            _costTypeBudgetState.value = UiState.Loading
            val result = costTypeRepository.getCostTypeBudgets()
            if (result.isSuccess) {
                Log.i("CostTypeViewModel", "Success: ${result.getOrNull()}")
                _costTypeBudgetState.value = UiState.Success(result.getOrNull()!!)
            } else {
                Log.i("CostTypeViewModel", "Error: ${result.exceptionOrNull()?.message}")
                _costTypeBudgetState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun setInitialBudget(costTypeName: String, budget: Double) {
        viewModelScope.launch {
            _addCostTypeBudgetState.value = UiState.Loading
            val result = costTypeRepository.setInitialBudget(costTypeName, budget)
            if (result.isSuccess) {
                Log.i("CostTypeViewModel", "Success: ${result.getOrNull()}")
                _addCostTypeBudgetState.value = UiState.Success(result.getOrNull()!!)
            } else {
                Log.i("CostTypeViewModel", "Error: ${result.exceptionOrNull()?.message}")
                _addCostTypeBudgetState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }

        }
    }

    fun setRemainingBudget(costTypeName: String, budget: Double) {
        viewModelScope.launch {
            _addCostTypeBudgetState.value = UiState.Loading
            val result = costTypeRepository.setRemainingBudget(costTypeName, budget)
            if (result.isSuccess) {
                Log.i("CostTypeViewModel", "Success: ${result.getOrNull()}")
                _addCostTypeBudgetState.value = UiState.Success(result.getOrNull()!!)
            } else {
                Log.i("CostTypeViewModel", "Error: ${result.exceptionOrNull()?.message}")
                _addCostTypeBudgetState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }

        }
    }

    fun resetRemainingBudget(costTypeName: String) {
        viewModelScope.launch {
            _addCostTypeBudgetState.value = UiState.Loading
            val result = costTypeRepository.resetRemainingBudget(costTypeName)
            if (result.isSuccess) {
                Log.i("CostTypeViewModel", "Success: ${result.getOrNull()}")
                _addCostTypeBudgetState.value = UiState.Success(result.getOrNull()!!)
            } else {
                Log.i("CostTypeViewModel", "Error: ${result.exceptionOrNull()?.message}")
                _addCostTypeBudgetState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }

        }
    }


    fun addCostTypeBudget(costTypeName: String, initialBudget : Double, maxCost: Double) {
        _addCostTypeBudgetState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = costTypeRepository.addCostTypeBudget(costTypeName, initialBudget, maxCost)
                if (result.isSuccess) {
                    Log.i("CostTypeViewModel", "Success: ${result.getOrNull()}")
                    _addCostTypeBudgetState.value = UiState.Success(result.getOrNull()!!)
                } else {
                    Log.i("CostTypeViewModel", "Error: ${result.exceptionOrNull()?.message}")
                    _addCostTypeBudgetState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _addCostTypeBudgetState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun setmaxCost(costTypeName: String, budget: Double) {
        viewModelScope.launch {
            _addCostTypeBudgetState.value = UiState.Loading
            val result = costTypeRepository.setmaxCost(costTypeName, budget)
            if (result.isSuccess) {
                Log.i("CostTypeViewModel", "Success: ${result.getOrNull()}")
                _addCostTypeBudgetState.value = UiState.Success(result.getOrNull()!!)
            } else {
                Log.i("CostTypeViewModel", "Error: ${result.exceptionOrNull()?.message}")
                _addCostTypeBudgetState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }

        }
    }
}