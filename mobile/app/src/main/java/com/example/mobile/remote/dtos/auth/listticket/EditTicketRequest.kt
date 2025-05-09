package com.example.mobile.remote.dtos.auth.listticket

data class EditTicketRequest (
    val ticketId: Int,
    val costType:String,
    val amount: Double,
    val description: String,
)