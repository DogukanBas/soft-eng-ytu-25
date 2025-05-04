package com.example.mobile.ui.accountant

import Ticket
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.ui.team_member.TeamMemberViewModel
import com.example.mobile.ui.team_member.TicketDetailFragment
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.MenuItem
import com.example.mobile.utils.UiState
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.CustomInput.InputListFragment
import com.token.uicomponents.CustomInput.InputValidator
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.input_menu_fragment.BtnOkVisibilityModes
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class TicketListFragment (val tickets:List<Int>): BaseFragment() {

    companion object {
        val TAG = "TicketListFragment"
    }

    private val viewModel: TeamMemberViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        setStateCollectors()
        val menuFragment = setMenu()
        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, menuFragment )
            .commit()
    }

    private fun setStateCollectors() {
        Log.i(TAG, "Setting up state collectors")
    }
    private fun getLogo(): Int {
        return R.drawable.ic_launcher_foreground
    }

    private fun setMenu(): NavigationListFragment {
        val menuItemsList = mutableListOf<IListMenuItem>()
        for (ticket in tickets){
            menuItemsList.add(MenuItem("Ticket ID: $ticket") {
                Log.i(TAG, "Ticket ID: $ticket button clicked")
                viewModel.getTicketDetails(ticket)
                viewModel.getApproveHistory(ticket)

                var hasHandledSuccess = false
                val loadingDialog = getDialog(DialogType.LOADING, "Loading")
                viewLifecycleOwner.lifecycleScope.launch {
                    combine(viewModel.getTicketState, viewModel.getApproveHistoryState) { state1, state2 ->
                        state1 to state2
                    }.collect { (state1, state2) ->

                        // Show loading if at least one is loading
                        if (state1 is UiState.Loading || state2 is UiState.Loading) {
                            if (!loadingDialog.isVisible) {
                                loadingDialog.show(
                                    requireActivity().supportFragmentManager,
                                    "LoadingDialog"
                                )
                            }
                            return@collect
                        } else {
                            loadingDialog.dismiss()
                        }

                        // Show error if any of them is error
                        if (state1 is UiState.Error) {
                            Log.e(TAG, "Error fetching ticket details: ${state1.message}")
                            showError(state1.message)
                            return@collect
                        }
                        if (state2 is UiState.Error) {
                            Log.e(TAG, "Error fetching approval history: ${state2.message}")
                            showError(state2.message)
                            return@collect
                        }

                        // Success only when both are success, and not already handled
                        if (!hasHandledSuccess &&
                            state1 is UiState.Success &&
                            state2 is UiState.Success
                        ) {
                            hasHandledSuccess = true
                            val ticketDetailFragment = TicketDetailFragment(state1.data,ticket, state2.data)
                            replaceFragment(ticketDetailFragment)
                        }

                    }
                }

            })
        }


        return NavigationListFragment(
            "List Tickets",
            true,
            menuItemsList,
            headerImage = getLogo(),
        )
    }

}


