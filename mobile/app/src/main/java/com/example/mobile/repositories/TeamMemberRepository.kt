package com.example.mobile.repositories

import Ticket
import com.example.mobile.remote.api.TeamMemberService
import com.example.mobile.remote.dtos.auth.createticket.CreateTicketResponse
import com.example.mobile.utils.toDto
import javax.inject.Inject

class TeamMemberRepository @Inject constructor(
    private val teamMemberService: TeamMemberService,
) {
    suspend fun getCostTypes() :Result<List<String>> {
         return try {
            val response = teamMemberService.getTeamMembers()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.costTypes)
            } else {
                Result.failure(Exception("Failed to fetch team members"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
    suspend fun createTicket(ticket: Ticket): Result<CreateTicketResponse> {
        return try {
            val response = teamMemberService.createTicket(ticket.toDto())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create ticket"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }






}