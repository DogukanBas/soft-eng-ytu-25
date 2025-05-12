package com.example.mobile.remote.dtos.Ticket.Create_Ticket

data class CreateTicketRequest (
    val costType : String,
    val amount : Double,
    val description: String,
    val date: String,
    val invoice: String


)
