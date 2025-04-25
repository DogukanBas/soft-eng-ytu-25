package com.example.mobile.remote.dtos.auth

data class CostTypeBudgetResponse(
    val name : String,
    val initialBudget: Double,
    val remainingBudget: Double
)


{
    override fun toString(): String {
        return name
    }

}

