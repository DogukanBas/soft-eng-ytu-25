package com.example.mobile.remote.dtos.auth

data class TicketWithoutInvoice (
    val costType:String,
    val amount: Double,
    val employeeId: String,
    val managerId: String
    )