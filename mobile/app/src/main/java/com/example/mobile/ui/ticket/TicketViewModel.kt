package com.example.mobile.ui.ticket

import Ticket
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.models.ApprovalHistoryItem
import com.example.mobile.remote.dtos.auth.TicketWithoutInvoice
import com.example.mobile.remote.dtos.auth.createticket.CreateTicketResponse
import com.example.mobile.repositories.TicketRepository
import com.example.mobile.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel(){
//    companion object {
//        const val TAG = "TeamMemberViewModel"
//    }
    private val _getCostType = MutableStateFlow<UiState<List<String>>>(UiState.Idle)
    val getCostType: StateFlow<UiState<List<String>>> = _getCostType


    private val _createTicket = MutableStateFlow<UiState<CreateTicketResponse>> (UiState.Idle)
    val createTicketState: StateFlow<UiState<CreateTicketResponse>> = _createTicket

    private val _getClosedTicketsIdState = MutableStateFlow<UiState<List<Int>>>(UiState.Idle)
    val getClosedTicketsIdState: StateFlow<UiState<List<Int>>> = _getClosedTicketsIdState

    private val _getTicketState = MutableStateFlow<UiState<TicketWithoutInvoice>> (UiState.Idle)
    val getTicketState: StateFlow<UiState<TicketWithoutInvoice>> = _getTicketState

    private val _getApproveHistoryState = MutableStateFlow<UiState<List<ApprovalHistoryItem>>>(UiState.Idle)
    val getApproveHistoryState: StateFlow<UiState<List<ApprovalHistoryItem>>> = _getApproveHistoryState
    fun getCostTypes() {

        viewModelScope.launch {

        _getCostType.value = UiState.Loading
        try {
            val result = ticketRepository.getCostTypes()
            if (result.isSuccess) {
                val teamMembers = result.getOrNull()
                _getCostType.value = UiState.Success(teamMembers ?: emptyList())
            } else {
                _getCostType.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            _getCostType.value = UiState.Error(e.toString())
        }
        Log.i("TAG", "Fetching team members")

        }

    }

    fun createTicket(ticket: Ticket) {
        viewModelScope.launch {
            _createTicket.value = UiState.Loading
            try {
                val result = ticketRepository.createTicket(ticket)
                if (result.isSuccess) {
                    val createTicketResponse = result.getOrNull()
                    _createTicket.value = UiState.Success(createTicketResponse!!)
                } else {
                    _createTicket.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _createTicket.value = UiState.Error(e.toString())
            }
        }


    }
    fun getClosedTicketsId(){
        viewModelScope.launch {
            _getClosedTicketsIdState.value = UiState.Loading
            try {
                val result = ticketRepository.getClosedTicketsId()
                if (result.isSuccess) {
                    val closedTickets = result.getOrNull()
                    _getClosedTicketsIdState.value = UiState.Success(closedTickets ?: emptyList())
                } else {
                    _getClosedTicketsIdState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _getClosedTicketsIdState.value = UiState.Error(e.toString())
            }
        }

    }
    fun getTicketDetails(ticketId: Int){
        viewModelScope.launch {
            _getTicketState.value = UiState.Loading
            try {
                val result = ticketRepository.getTicket(ticketId)
                if (result.isSuccess) {
                    val ticket = result.getOrNull()
                    _getTicketState.value = UiState.Success(ticket!!)
                } else {
                    _getTicketState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _getTicketState.value = UiState.Error(e.toString())
            }
        }

    }
    fun getApproveHistory(ticketId: Int){
        viewModelScope.launch {
            _getApproveHistoryState.value = UiState.Loading
            try {
                val result = ticketRepository.getApproveHistory(ticketId)
                if (result.isSuccess) {
                    val approveHistory = result.getOrNull()
                    _getApproveHistoryState.value = UiState.Success(approveHistory!!)
                } else {
                    _getApproveHistoryState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _getApproveHistoryState.value = UiState.Error(e.toString())
            }
        }
    }
}