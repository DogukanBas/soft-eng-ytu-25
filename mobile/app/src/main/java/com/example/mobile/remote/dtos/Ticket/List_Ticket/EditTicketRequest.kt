package com.example.mobile.remote.dtos.Ticket.List_Ticket

data class EditTicketRequest (
    val ticketId: Int,
    val costType:String,
    val amount: Double,
    val description: String,
)