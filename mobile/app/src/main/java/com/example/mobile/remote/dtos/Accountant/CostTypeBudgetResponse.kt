package com.example.mobile.remote.dtos.Accountant

data class CostTypeBudgetResponse(
    val name : String,
    val initialBudget: Double,
    val remainingBudget: Double,
    val maxCost: Double
)


{
    override fun toString(): String {
        return name
    }

}

