package com.example.mobile.ui.team_member

import Ticket
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.remote.dtos.auth.TicketWithoutInvoice
import com.example.mobile.remote.dtos.auth.createticket.CreateTicketResponse
import com.example.mobile.repositories.TeamMemberRepository
import com.example.mobile.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamMemberViewModel @Inject constructor(
    private val teamMemberRepository: TeamMemberRepository
) : ViewModel(){
//    companion object {
//        const val TAG = "TeamMemberViewModel"
//    }
    private val _getCostTypeTeamMembers = MutableStateFlow<UiState<List<String>>>(UiState.Idle)
    val getCostTypeTeamMembers: StateFlow<UiState<List<String>>> = _getCostTypeTeamMembers


    private val _createTicket = MutableStateFlow<UiState<CreateTicketResponse>> (UiState.Idle)
    val createTicketState: StateFlow<UiState<CreateTicketResponse>> = _createTicket

    private val _getClosedTicketsIdState = MutableStateFlow<UiState<List<Int>>>(UiState.Idle)
    val getClosedTicketsIdState: StateFlow<UiState<List<Int>>> = _getClosedTicketsIdState

    private val _getTicketState = MutableStateFlow<UiState<TicketWithoutInvoice>> (UiState.Idle)
    val getTicketState: StateFlow<UiState<TicketWithoutInvoice>> = _getTicketState
    fun getCostTypes() {
        // Logic to fetch team members
        viewModelScope.launch {

        _getCostTypeTeamMembers.value = UiState.Loading
        try {
//            val result:List<String> = listOf("Bus","Taxi","hotel")
//            _getCostTypeTeamMembers.value = UiState.Success(result)

            val result = teamMemberRepository.getCostTypes()
            if (result.isSuccess) {
                val teamMembers = result.getOrNull()
                _getCostTypeTeamMembers.value = UiState.Success(teamMembers ?: emptyList())
            } else {
                _getCostTypeTeamMembers.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            _getCostTypeTeamMembers.value = UiState.Error(e.toString())
        }
        Log.i("TAG", "Fetching team members")

        }

    }

    fun createTicket(ticket: Ticket) {
        viewModelScope.launch {
            _createTicket.value = UiState.Loading
            try {
                val result = teamMemberRepository.createTicket(ticket)
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
                val result = teamMemberRepository.getClosedTicketsId()
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
                val result = teamMemberRepository.getTicket(ticketId)
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

}