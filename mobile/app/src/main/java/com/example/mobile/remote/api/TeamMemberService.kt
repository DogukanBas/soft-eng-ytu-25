package com.example.mobile.remote.api

import com.example.mobile.remote.dtos.auth.createticket.CreateTicketRequest
import com.example.mobile.remote.dtos.auth.createticket.CreateTicketResponse
import com.example.mobile.remote.dtos.auth.createticket.TeamMemberCostTypeResponseList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TeamMemberService {
    @GET("api/ticket/cost-types")
    suspend fun getTeamMembers(): Response<TeamMemberCostTypeResponseList>

    @POST("api/ticket/create-ticket")
    suspend fun createTicket(@Body request: CreateTicketRequest): Response<CreateTicketResponse>


}