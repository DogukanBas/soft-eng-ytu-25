package com.example.mobile.ui.team_member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.adapters.ApprovalHistoryAdapter
import com.example.mobile.models.ApprovalHistoryItem
import com.example.mobile.remote.dtos.auth.TicketWithoutInvoice
import com.example.mobile.ui.BaseFragment
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class TicketDetailFragment(private val ticket: TicketWithoutInvoice, private val ticketId:Int, private val approveHistory: List<ApprovalHistoryItem>) : BaseFragment() {

    private val viewModel: TeamMemberViewModel by viewModels()
    private lateinit var ticketCostType: TextView
    private lateinit var ticketAmount: TextView
    private lateinit var ticketEmployeeId: TextView
    private lateinit var ticketManagerId: TextView
    private lateinit var ticketStatus: TextView
    private lateinit var recyclerView: RecyclerView

    // Action buttons
    private lateinit var btnAccept: Button
    private lateinit var btnReject: Button
    private lateinit var btnCancel: Button
    private lateinit var btnEdit: Button

    companion object {
        val TAG = "TicketDetailFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_ticket_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated for ticket ID: ${ticketId}")

        // Initialize views
        initViews(view)

        displayTicketDetails(ticket)
        displayDummyApprovalHistory()

    }

    private fun initViews(view: View) {
        // Ticket info views
        ticketCostType = view.findViewById(R.id.ticket_cost_type)
        ticketAmount = view.findViewById(R.id.ticket_amount)
        ticketEmployeeId = view.findViewById(R.id.ticket_employee_id)
        ticketManagerId = view.findViewById(R.id.ticket_manager_id)
        ticketStatus = view.findViewById(R.id.ticket_status)

        // RecyclerView for approval history
        recyclerView = view.findViewById(R.id.approval_history_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Buttons
        btnAccept = view.findViewById(R.id.btn_accept)
        btnReject = view.findViewById(R.id.btn_reject)
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnEdit = view.findViewById(R.id.btn_edit)

        // Initially hide all buttons - they will be shown based on the ticket state
        btnAccept.visibility = View.GONE
        btnReject.visibility = View.GONE
        btnCancel.visibility = View.GONE
        btnEdit.visibility = View.GONE

        // Set up button click listeners (to be implemented later)
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        btnAccept.setOnClickListener {
            Log.i(TAG, "Accept button clicked for ticket ID: $ticketId")
            // To be implemented
        }

        btnReject.setOnClickListener {
            Log.i(TAG, "Reject button clicked for ticket ID: $ticketId")
            // To be implemented
        }

        btnCancel.setOnClickListener {
            Log.i(TAG, "Cancel button clicked for ticket ID: $ticketId")
            // To be implemented
        }

        btnEdit.setOnClickListener {
            Log.i(TAG, "Edit button clicked for ticket ID: $ticketId")
            // To be implemented
        }
    }


    private fun displayTicketDetails(ticket: TicketWithoutInvoice) {
        Log.i(TAG, "Displaying ticket details: $ticket")
        ticketCostType.text = ticket.costType
        ticketAmount.text = "₺${String.format(Locale.getDefault(), "%.2f", ticket.amount)}"
        ticketEmployeeId.text = ticket.employeeId
        ticketManagerId.text = ticket.managerId
        ticketStatus.text = approveHistory.lastOrNull()?.status ?: "UNKNOWN"
        val context = ticketStatus.context
        val color = when {
            ticketStatus.text.contains("APPROVED") || ticketStatus.text.contains("SENT_TO_ACCOUNTANT") ->
                android.R.color.holo_green_dark
            ticketStatus.text.contains("REJECTED") || ticketStatus.text.contains("CANCELED") ->
                android.R.color.holo_red_dark
            else -> android.R.color.black
        }
        ticketStatus.setTextColor(context.resources.getColor(color, context.theme))
        updateActionButtonsVisibility("CLOSED_AS_APPROVED")
    }

    private fun updateActionButtonsVisibility(status: String) {
        // This logic would depend on the ticket's current status and the user's role
        // For now, just showing/hiding buttons based on status example
        when (status) {
            "SENT_TO_MANAGER" -> {
                // If user is a manager, show Accept and Reject buttons
                btnAccept.visibility = View.VISIBLE
                btnReject.visibility = View.VISIBLE
                btnCancel.visibility = View.GONE
                btnEdit.visibility = View.GONE
            }
            "REJECTED_BY_MANAGER_CAN_BE_FIXED" -> {
                // If user is the creator, show Edit button
                btnAccept.visibility = View.GONE
                btnReject.visibility = View.GONE
                btnCancel.visibility = View.VISIBLE
                btnEdit.visibility = View.VISIBLE
            }
            else -> {
                // For closed/approved tickets, hide all action buttons
                btnAccept.visibility = View.GONE
                btnReject.visibility = View.GONE
                btnCancel.visibility = View.GONE
                btnEdit.visibility = View.GONE
            }
        }
    }

    // For demonstration purposes, this method creates dummy approval history data
    // In a real implementation, this would come from the API
    private fun displayDummyApprovalHistory() {

        val adapter = ApprovalHistoryAdapter(approveHistory)
        recyclerView.adapter = adapter
    }
}