package com.example.mobile.remote.dtos.auth.createticket

data class CreateTicketRequest (
    val costType : String,
    val amount : Double,
    val description: String,
    val date: String,
    val invoice: String


)
