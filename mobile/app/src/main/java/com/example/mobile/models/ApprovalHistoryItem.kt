package com.example.mobile.models

import java.time.LocalDate

/**
 * Data class representing an entry in the approval history timeline of a ticket
 */
data class ApprovalHistoryItem(
    val id: Int,
    val status: String,
    val date: LocalDate,
    val actorId: String,
    val actorRole: String,
    val description: String? = null
)