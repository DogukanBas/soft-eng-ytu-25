package com.example.mobile.ui.team_member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.ui.accountant.TicketListMenuFragment
import com.example.mobile.ui.login.LoginFragment
import com.example.mobile.ui.notification.NotificationFragment
import com.example.mobile.ui.notification.NotificationViewModel
import com.example.mobile.ui.ticket.CreateTicketFragment
import com.example.mobile.ui.ticket.TicketViewModel
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.MenuItem
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamMemberMenuFragment : BaseFragment() {

    companion object {
        val TAG = "TeamMemberMenuFragment"
    }
    private val ticketViewModel: TicketViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        val menuFragment = setMenu()

        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, menuFragment)
            .commit()
    }

    private fun getLogo(): Int {
        return R.drawable.ic_launcher_foreground
    }

    private fun setMenu(): NavigationListFragment {
        val menuItems = mutableListOf<IListMenuItem>()
        menuItems.add(MenuItem(
            "Create Ticket") {
            Log.i(TAG, "Create Ticket button clicked")
            ticketViewModel.getCostTypes()
                observeUiState(
                    ticketViewModel.getCostType,
                    onSuccess = { data ->
                        if(data.isEmpty()){
                            getDialog(
                                DialogType.ERROR,
                                "No cost types found"
                            ).show(
                                childFragmentManager,
                                "ErrorDialog"
                            )
                        }
                        else{
                        replaceFragment(
                            CreateTicketFragment(data)
                        )}
                    },
                    onError = {
                        Log.e(TAG, "Error fetching team members: $it")
                        popFragment()
                    },
                    onLoading = {
                        showLoading()
                    },
                    onIdle = {
                        hideLoading()
                    }
                )
        })
        menuItems.add(MenuItem(
            "List Tickets") {
            replaceFragment(
                TicketListMenuFragment()
            )
        })
        menuItems.add(MenuItem(
            "Notifications") {
            Log.i(TAG, "Notifications button clicked")
            notificationViewModel.getUserNotification()
                observeUiState(
                    notificationViewModel.notificationState,
                    onSuccess = { data ->
                        replaceFragment(NotificationFragment(data))
                    },
                    onError = {
                        Log.e(TAG, "Error fetching team members: $it")
                        popFragment()
                    },
                    onLoading = {
                        showLoading()
                    },
                    onIdle = {
                        hideLoading()
                    }
                )
        })
        menuItems.add(MenuItem(
            "Log Out") {
            Log.i(TAG, "Log Out button clicked")
            replaceFragment(
                LoginFragment(),
                addToBackStack = false
            )
        })

        return NavigationListFragment(
            "Team-Member Menu",
            true,
            menuItems,
            headerImage= getLogo(),
        )
    }
}

