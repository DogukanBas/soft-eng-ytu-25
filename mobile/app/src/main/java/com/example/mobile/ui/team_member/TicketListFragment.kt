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
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.CustomInput.InputListFragment
import com.token.uicomponents.CustomInput.InputValidator
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.input_menu_fragment.BtnOkVisibilityModes
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment
import dagger.hilt.android.AndroidEntryPoint
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
                observeUiState(
                    viewModel.getTicketState,
                    onSuccess = { data ->
                        Log.i(TAG, "Ticket details fetched successfully: $data")
                        System.out.print("as")
                        replaceFragment(
                            TicketDetailFragment(data,ticket)
                        )
                    }
                    , onError = {
                        Log.e(TAG, "Error fetching ticket details: $it")
                        popFragment()
                        getDialog(DialogType.ERROR,it).show(requireActivity().supportFragmentManager, "ErrorDialog")

                    },
                )
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


