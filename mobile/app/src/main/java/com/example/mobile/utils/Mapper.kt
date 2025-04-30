package com.example.mobile.utils

import Ticket
import com.example.mobile.model.User.Employee
import com.example.mobile.remote.dtos.auth.AddUserRequest
import com.example.mobile.remote.dtos.auth.createticket.CreateTicketRequest

fun Employee.toDto(): AddUserRequest {
    return AddUserRequest(
        name = this.name,
        surname = this.surname,
        personalNo = this.personalNo,
        email = this.email,
        password = this.password,
        userType = this.userType.toString(),
        deptName = this.department
    )
}
fun Ticket.toDto(): CreateTicketRequest {
    return CreateTicketRequest(
        costType = this.costType,
        amount = this.amount,
        description = this.description,
        date = this.date,
        invoice = this.invoice
    )
}
