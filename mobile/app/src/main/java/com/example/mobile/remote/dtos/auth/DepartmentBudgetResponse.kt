package com.example.mobile.remote.dtos.auth

import com.example.mobile.model.User.Employee
import com.example.mobile.model.User.User

data class DepartmentBudgetResponse (
    val name:String,
    val initialBudget : Double,
    val remainingBudget : Double
)
{
    override fun toString(): String {
        return name
    }
}