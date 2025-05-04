package com.example.mobile.repositories

import Ticket
import android.util.Log
import com.example.mobile.models.ApprovalHistoryItem
import com.example.mobile.remote.api.TicketService
import com.example.mobile.remote.dtos.auth.TicketWithoutInvoice
import com.example.mobile.remote.dtos.auth.createticket.CreateTicketResponse
import com.example.mobile.utils.toDto
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketService: TicketService,
) {
    suspend fun getCostTypes() :Result<List<String>> {
         return try {
            val response = ticketService.getTeamMembers()
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
            val response = ticketService.createTicket(ticket.toDto())
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
    suspend fun getClosedTicketsId(): Result<List<Int>> {
        return try {
            val response = ticketService.getClosedTickets()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.ticketIds)
            } else {

                    Log.i("TAG", "Error: ${response.headers().get("message")}, ")
                    Result.failure(Exception(response.headers().get("message")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getTicket(ticketId: Int): Result<TicketWithoutInvoice> {
        return try {
            val response = ticketService.getTicket(ticketId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Log.i("TAG", "Error: ${response.headers().get("message")}, ")
                Result.failure(Exception(response.headers().get("message")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getApproveHistory(ticketId: Int): Result<List<ApprovalHistoryItem>> {
        return try {
            val response = ticketService.getApproveHistory(ticketId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Log.i("TAG", "Error: ${response.headers().get("message")}, ")
                Result.failure(Exception(response.headers().get("message")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }






}