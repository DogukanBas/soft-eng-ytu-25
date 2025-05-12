package com.example.mobile.remote.dtos.Accountant

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