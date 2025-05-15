package com.example.mobile.ui.Report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.remote.dtos.Report.EntityResponse
import com.example.mobile.remote.dtos.Report.ReportResponse
import com.example.mobile.repositories.ReportRepository
import com.example.mobile.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {
    private val _getEntityListUiState = MutableStateFlow<UiState<List<EntityResponse>>>(UiState.Idle)
    val getEntity: StateFlow<UiState<List<EntityResponse>>> = _getEntityListUiState

    private val _getReportUiState = MutableStateFlow<UiState<List<ReportResponse>>>(UiState.Idle)
    val getReportUiState: StateFlow<UiState<List<ReportResponse>>> = _getReportUiState
    fun getEntities(type: String) {
        viewModelScope.launch{
            _getEntityListUiState.value = UiState.Loading
            try {
                val response = reportRepository.getEntities(type)
                if (response.isSuccess ) {
                    _getEntityListUiState.value = UiState.Success(response.getOrNull()!!)
                } else {
                    _getEntityListUiState.value = UiState.Error("Error: ${response.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _getEntityListUiState.value = UiState.Error("Exception: ${e.message}")
            }

        }
    }

    fun getReport(type: String, id: String) {
        viewModelScope.launch{
            _getReportUiState.value = UiState.Loading
            try {
                val response = reportRepository.getReport(type,id)
                if (response.isSuccess ) {
                    _getReportUiState.value = UiState.Success(response.getOrNull()!!)
                } else {
                    _getReportUiState.value = UiState.Error("Error: ${response.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _getReportUiState.value = UiState.Error("Exception: ${e.message}")
            }

        }
    }
}