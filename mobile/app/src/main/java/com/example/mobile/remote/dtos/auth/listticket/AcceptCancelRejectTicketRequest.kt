package com.example.mobile.remote.dtos.auth.listticket

data class AcceptCancelRejectTicketRequest (
    val ticketId: Int,
    val reason: String
    )