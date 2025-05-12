package com.example.mobile.model.Ticket

data class Ticket (
    val costType : String,
    val amount : Double,
    val description: String,
    val date: String,
    var invoice: String

)