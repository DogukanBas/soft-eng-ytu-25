package com.example.mobile.remote.dtos.Ticket

data class TicketWithoutInvoice (
    val costType:String,
    val amount: Double,
    val employeeId: String,
    val managerId: String
    )