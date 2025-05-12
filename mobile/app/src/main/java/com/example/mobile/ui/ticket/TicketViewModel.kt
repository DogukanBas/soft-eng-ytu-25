package com.example.mobile.ui.ticket

import com.example.mobile.model.Ticket.Ticket
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.model.Ticket.ApprovalHistoryItem
import com.example.mobile.remote.dtos.Ticket.TicketWithoutInvoice
import com.example.mobile.remote.dtos.Ticket.Create_Ticket.CreateTicketResponse
import com.example.mobile.remote.dtos.Ticket.List_Ticket.EditTicketRequest
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

    private val _getTicketListIdState = MutableStateFlow<UiState<List<Int>>>(UiState.Idle)
    val getTicketListIdState: StateFlow<UiState<List<Int>>> = _getTicketListIdState

    private val _getTicketState = MutableStateFlow<UiState<TicketWithoutInvoice>> (UiState.Idle)
    val getTicketState: StateFlow<UiState<TicketWithoutInvoice>> = _getTicketState

    private val _getApproveHistoryState = MutableStateFlow<UiState<List<ApprovalHistoryItem>>>(UiState.Idle)
    val getApproveHistoryState: StateFlow<UiState<List<ApprovalHistoryItem>>> = _getApproveHistoryState

    private val _ticketState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val ticketState: StateFlow<UiState<String>> = _ticketState

    private val _getInvoiceState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val getInvoiceState: StateFlow<UiState<String>> = _getInvoiceState

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

    // New methods for ticket actions
    fun rejectTicketClose(ticketId: Int, reason: String) {
        viewModelScope.launch {
            _ticketState.value = UiState.Loading
            try {
                val result = ticketRepository.rejectTicketClose(ticketId, reason)
                if (result.isSuccess) {
                    _ticketState.value = UiState.Success(result.getOrNull()!!)
                } else {
                    _ticketState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Failed to reject ticket")
                }
            } catch (e: Exception) {
                _ticketState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun rejectTicketEdit(ticketId: Int, reason: String) {
        viewModelScope.launch {
            _ticketState.value = UiState.Loading
            try {
                val result = ticketRepository.rejectTicketEdit(ticketId, reason)
                if (result.isSuccess) {
                    _ticketState.value = UiState.Success(result.getOrNull()!!)
                } else {
                    _ticketState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Failed to reject ticket")
                }
            } catch (e: Exception) {
                _ticketState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun acceptTicket(ticketId: Int,reason: String) {
        viewModelScope.launch {
            _ticketState.value = UiState.Loading
            try {
                val result = ticketRepository.acceptTicket(ticketId,reason)
                if (result.isSuccess) {
                    _ticketState.value = UiState.Success(result.getOrNull()!!)
                } else {
                    _ticketState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Failed to accept ticket")
                }
            } catch (e: Exception) {
                _ticketState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun cancelTicket(ticketId: Int, reason: String) {
        viewModelScope.launch {
            _ticketState.value = UiState.Loading
            try {
                val result = ticketRepository.cancelTicket(ticketId, reason)
                if (result.isSuccess) {
                    _ticketState.value = UiState.Success(result.getOrNull()!!)
                } else {
                    _ticketState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Failed to cancel ticket")
                }
            } catch (e: Exception) {
                _ticketState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun getClosedCreatedTicketsId(){
        viewModelScope.launch {
            _getTicketListIdState.value = UiState.Loading
            try {
                val result = ticketRepository.getClosedCreatedTicketsId()
                if (result.isSuccess) {
                    val closedTickets = result.getOrNull()
                    _getTicketListIdState.value = UiState.Success(closedTickets ?: emptyList())
                } else {
                    _getTicketListIdState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _getTicketListIdState.value = UiState.Error(e.toString())
            }
        }

    }
    fun getActiveCreatedTicketsId(){
        viewModelScope.launch {
            _getTicketListIdState.value = UiState.Loading
            try {
                val result = ticketRepository.getActiveCreatedTicketsId()
                if (result.isSuccess) {
                    val activeTickets = result.getOrNull()
                    _getTicketListIdState.value = UiState.Success(activeTickets ?: emptyList())
                } else {
                    _getTicketListIdState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _getTicketListIdState.value = UiState.Error(e.toString())
            }
        }
    }

    fun getClosedAssignedTicketsId(){
        viewModelScope.launch {
            _getTicketListIdState.value = UiState.Loading
            try {
                val result = ticketRepository.getClosedAssignedTicketsId()
                if (result.isSuccess) {
                    val closedTickets = result.getOrNull()
                    _getTicketListIdState.value = UiState.Success(closedTickets ?: emptyList())
                } else {
                    _getTicketListIdState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _getTicketListIdState.value = UiState.Error(e.toString())
            }
        }

    }
    fun getActiveAssignedTicketsId(){
        viewModelScope.launch {
            _getTicketListIdState.value = UiState.Loading
            try {
                val result = ticketRepository.getActiveAssignedTicketsId()
                if (result.isSuccess) {
                    val activeTickets = result.getOrNull()
                    _getTicketListIdState.value = UiState.Success(activeTickets ?: emptyList())
                } else {
                    _getTicketListIdState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _getTicketListIdState.value = UiState.Error(e.toString())
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
    fun getInvoice(ticketId: Int){
        viewModelScope.launch {
            _getInvoiceState.value = UiState.Loading
            try {
                val result = ticketRepository.getInvoice(ticketId)
                if (result.isSuccess) {
                    val invoice = result.getOrNull()
                    _getInvoiceState.value = UiState.Success(invoice!!)
                } else {
                    _getInvoiceState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _getInvoiceState.value = UiState.Error(e.toString())
            }
        }
    }

    fun editTicket(ticket: EditTicketRequest){
        viewModelScope.launch {
            _createTicket.value = UiState.Loading
            try {
                val result = ticketRepository.editTicket(ticket)
                if (result.isSuccess) {
                    _createTicket.value = UiState.Success(result.getOrNull()!!)
                } else {
                    _createTicket.value = UiState.Error(result.exceptionOrNull()?.message ?: "Failed to edit ticket")
                }
            } catch (e: Exception) {
                _createTicket.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun editCostTypeTicket(ticketid:Int,costType:String) {
        viewModelScope.launch {
            _createTicket.value = UiState.Loading
            try {
                val result = ticketRepository.editCostTypeTicket(ticketid,costType)
                if (result.isSuccess) {
                    _createTicket.value = UiState.Success(result.getOrNull()!!)
                } else {
                    _createTicket.value = UiState.Error(result.exceptionOrNull()?.message ?: "Failed to edit ticket")
                }
            } catch (e: Exception) {
                _createTicket.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}