package com.example.mobile.ui.manager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.ui.ticket.TicketViewModel
import com.example.mobile.utils.MenuItem
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListAssignedTicketsFragment (): BaseFragment() {

    companion object {
        val TAG = "ListTicketFragment"
    }

    private val viewModel: TicketViewModel by viewModels()
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
            .replace(R.id.input_menu_container, menuFragment )
            .commit()
    }

    private fun getLogo(): Int {
        return R.drawable.ic_launcher_foreground
    }

    private fun setMenu(): NavigationListFragment {
        val menuItemsList = mutableListOf<IListMenuItem>()
        menuItemsList.add(MenuItem("List Active Tickets"){getTicketList(viewModel){viewModel.getActiveAssignedTicketsId()} })
        menuItemsList.add(MenuItem("List Closed Tickets"){getTicketList(viewModel){viewModel.getClosedAssignedTicketsId()} })

        return NavigationListFragment(
            "List Assigned Tickets",
            true,
            menuItemsList,
            headerImage = getLogo(),
        )

    }
}


