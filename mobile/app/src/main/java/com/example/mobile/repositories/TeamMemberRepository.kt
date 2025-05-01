package com.example.mobile.repositories

import Ticket
import android.util.Log
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
                if(response.body() != null) {
                    Log.i("TAG", "Error: ${response.body()!!.message}, ${response.message()}")
                    Result.failure(Exception(response.message()))
                }
                else {
                    Log.i("TAG", "Error: ${response.headers().get("message")}, ")
                    Result.failure(Exception(response.headers().get("message")))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }






}