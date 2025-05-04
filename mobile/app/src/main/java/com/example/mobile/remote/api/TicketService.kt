package com.example.mobile.remote.api

import com.example.mobile.models.ApprovalHistoryItem
import com.example.mobile.remote.dtos.auth.TicketWithoutInvoice
import com.example.mobile.remote.dtos.auth.createticket.CreateTicketRequest
import com.example.mobile.remote.dtos.auth.createticket.CreateTicketResponse
import com.example.mobile.remote.dtos.auth.createticket.TeamMemberCostTypeResponseList
import com.example.mobile.remote.dtos.auth.listticket.ListTicketIdResponseList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TicketService {
    @GET("api/ticket/cost-types")
    suspend fun getTeamMembers(): Response<TeamMemberCostTypeResponseList>

    @POST("api/ticket/create-ticket")
    suspend fun createTicket(@Body request: CreateTicketRequest): Response<CreateTicketResponse>

    @GET("api/ticket/closed-tickets")
    suspend fun getClosedTickets(): Response<ListTicketIdResponseList>
    @GET("api/ticket")
    suspend fun getTicket(@Query("ticketId") ticketId: Int): Response<TicketWithoutInvoice>

    @GET("api/ticket/approve-history")
    suspend fun getApproveHistory(@Query("ticketId") ticketId: Int): Response<List<ApprovalHistoryItem>>

}