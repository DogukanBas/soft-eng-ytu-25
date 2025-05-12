package com.example.mobile.repositories

import com.example.mobile.model.Ticket.Ticket
import android.util.Log
import com.example.mobile.model.Ticket.ApprovalHistoryItem
import com.example.mobile.remote.api.TicketService
import com.example.mobile.remote.dtos.Ticket.TicketWithoutInvoice
import com.example.mobile.remote.dtos.Ticket.Create_Ticket.CreateTicketResponse
import com.example.mobile.remote.dtos.Ticket.List_Ticket.EditTicketRequest
import com.example.mobile.remote.dtos.Ticket.List_Ticket.TicketActionRequest
import com.example.mobile.utils.toDto
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketService: TicketService,
) {
    companion object {
        private const val TAG = "TicketRepository"
    }
    
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
                    Log.i(TAG, "Error: ${response.body()!!.message}, ${response.message()}")
                    Result.failure(Exception(response.message()))
                }
                else {
                    Log.i(TAG, "Error: ${response.headers().get("message")}, ")
                    Result.failure(Exception(response.headers().get("message")))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // New ticket action methods
    suspend fun acceptTicket(ticketId: Int,reason: String): Result<String> {
        return try {
            val response = ticketService.acceptTicket(TicketActionRequest(ticketId, reason))
            if (response.isSuccessful) {
                Log.i(TAG, "Ticket $ticketId accepted successfully")
                Result.success(response.body()!!.message)
            } else {
                Log.e(TAG, "Error accepting ticket $ticketId: ${response.headers().get("message")}")
                Result.failure(Exception(response.headers().get("message") ?: "Failed to accept ticket"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception accepting ticket $ticketId", e)
            Result.failure(e)
        }
    }
    
    suspend fun rejectTicketClose(ticketId: Int, reason: String): Result<String> {
        return try {
            val response = ticketService.rejectTicketClose(TicketActionRequest(ticketId, reason))
            if (response.isSuccessful) {
                Log.i(TAG, "Ticket $ticketId rejected and closed successfully")
                Result.success(response.body()!!.message)
            } else {
                Log.e(TAG, "Error rejecting ticket $ticketId: ${response.headers().get("message")}")
                Result.failure(Exception(response.headers().get("message") ?: "Failed to reject ticket"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception rejecting ticket $ticketId", e)
            Result.failure(e)
        }
    }
    
    suspend fun rejectTicketEdit(ticketId: Int, reason: String): Result<String> {
        return try {
            val response = ticketService.rejectTicketEdit(TicketActionRequest(ticketId, reason))
            if (response.isSuccessful) {
                Log.i(TAG, "Ticket $ticketId rejected and sent for edit successfully")
                Result.success(response.body()!!.message)
            } else {
                Log.e(TAG, "Error rejecting ticket $ticketId: ${response.headers().get("message")}")
                Result.failure(Exception(response.headers().get("message") ?: "Failed to reject ticket"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception rejecting ticket $ticketId", e)
            Result.failure(e)
        }
    }
    
    suspend fun cancelTicket(ticketId: Int, reason: String): Result<String> {
        return try {
            val response = ticketService.cancelTicket(TicketActionRequest(ticketId, reason))
            if (response.isSuccessful) {
                Log.i(TAG, "Ticket $ticketId cancelled successfully")
                Result.success(response.body()!!.message)
            } else {
                Log.e(TAG, "Error cancelling ticket $ticketId: ${response.headers().get("message")}")
                Result.failure(Exception(response.headers().get("message") ?: "Failed to cancel ticket"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception cancelling ticket $ticketId", e)
            Result.failure(e)
        }
    }
    
    suspend fun getClosedCreatedTicketsId(): Result<List<Int>> {
        return try {
            val response = ticketService.getClosedCreatedTickets()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.ticketIds)
            } else {

                Log.i(TAG, "Error: ${response.headers().get("message")}, ")
                Result.failure(Exception(response.headers().get("message")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getActiveCreatedTicketsId(): Result<List<Int>> {
        return try {
            val response = ticketService.getActiveCreatedTickets()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.ticketIds)
            } else {
                Log.i(TAG, "Error: ${response.headers().get("message")}, ")
                Result.failure(Exception(response.headers().get("message")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getClosedAssignedTicketsId(): Result<List<Int>> {
        return try {
            val response = ticketService.getClosedAssignedTickets()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.ticketIds)
            } else {
                Log.i(TAG, "Error: ${response.headers().get("message")}, ")
                Result.failure(Exception(response.headers().get("message")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getActiveAssignedTicketsId(): Result<List<Int>> {
        return try {
            val response = ticketService.getActiveAssignedTickets()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.ticketIds)
            } else {
                Log.i(TAG, "Error: ${response.headers().get("message")}, ")
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
                Log.i(TAG, "Error: ${response.headers().get("message")}, ")
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
                Log.i(TAG, "Error: ${response.headers().get("message")}, ")
                Result.failure(Exception(response.headers().get("message")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getInvoice(ticketId : Int) : Result<String> {
        return try {
            val response = ticketService.getTicketInvoice(ticketId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.invoice)
            } else {
                Log.i(TAG, "Error: ${response.headers().get("message")}, ")
                Result.failure(Exception(response.headers().get("message")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editTicket(ticket: EditTicketRequest):Result<CreateTicketResponse>{
        return try {
            val response = ticketService.editTicket(ticket)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Log.i(TAG, "Error: ${response.headers().get("message")}, ")
                Result.failure(Exception(response.headers().get("message")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun editCostTypeTicket(ticketId:Int, costType:String):Result<CreateTicketResponse>{
        return try {
            val response = ticketService.editCostTypeTicket(ticketId,costType)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Log.i(TAG, "Error: ${response.headers().get("message")}, ")
                Result.failure(Exception(response.headers().get("message")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}