package com.example.mobile.remote.api

import com.example.mobile.models.ApprovalHistoryItem
import com.example.mobile.remote.dtos.auth.TicketWithoutInvoice
import com.example.mobile.remote.dtos.auth.createticket.CreateTicketRequest
import com.example.mobile.remote.dtos.auth.createticket.CreateTicketResponse
import com.example.mobile.remote.dtos.auth.createticket.TeamMemberCostTypeResponseList
import com.example.mobile.remote.dtos.auth.listticket.EditTicketRequest
import com.example.mobile.remote.dtos.auth.listticket.TicketActionRequest
import com.example.mobile.remote.dtos.auth.listticket.ListTicketIdResponseList
import com.example.mobile.remote.dtos.auth.listticket.TicketActionResponse
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

    @GET("api/ticket")
    suspend fun getTicket(@Query("ticketId") ticketId: Int): Response<TicketWithoutInvoice>

    @GET("api/ticket/approve-history")
    suspend fun getApproveHistory(@Query("ticketId") ticketId: Int): Response<List<ApprovalHistoryItem>>

    @GET("api/ticket/created/closed")
    suspend fun getClosedCreatedTickets(): Response<ListTicketIdResponseList>
    
    @GET("api/ticket/created/active")
    suspend fun getActiveCreatedTickets(): Response<ListTicketIdResponseList>
    
    @GET("api/ticket/assigned/closed")
    suspend fun getClosedAssignedTickets(): Response<ListTicketIdResponseList>
    
    @GET("api/ticket/assigned/active")
    suspend fun getActiveAssignedTickets(): Response<ListTicketIdResponseList>

    // New ticket action endpoints
    @POST("api/ticket/approve")
    suspend fun acceptTicket(
        @Body request: TicketActionRequest
    ): Response<TicketActionResponse>
    
    @POST("api/ticket/reject-and-close")
    suspend fun rejectTicketClose(
        @Body request: TicketActionRequest

    ): Response<TicketActionResponse>
    
    @POST("api/ticket/reject-and-return")
    suspend fun rejectTicketEdit(
        @Body request: TicketActionRequest
    ): Response<TicketActionResponse>
    
    @POST("api/ticket/cancel")
    suspend fun cancelTicket(
        @Body request: TicketActionRequest

    ): Response<TicketActionResponse>

    @POST("api/ticket/edit")
    suspend fun editTicket(
        @Body request: EditTicketRequest
    ): Response<CreateTicketResponse>

    @POST("api/ticket/edit-cost-type")
    suspend fun editCostTypeTicket(
        @Query ("ticketId") ticketId: Int,
        @Query ("costType") costType: String
    ): Response<CreateTicketResponse>
}