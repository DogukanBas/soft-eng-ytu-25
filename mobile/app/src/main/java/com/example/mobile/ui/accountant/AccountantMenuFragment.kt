package com.example.mobile.ui.accountant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.ui.manager.ListAssignedTicketsFragment
import com.example.mobile.utils.MenuItem
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountantMenuFragment : BaseFragment() {

    companion object {
        val TAG = "AccountantMenuFragment"
    }
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
            "List Assigned Tickets") {
            Log.i(TAG, "List Team Tickets button clicked")
            replaceFragment(
                ListAssignedTicketsFragment()
            )
        })
        menuItems.add(MenuItem(
            "Set Budgets") {
                val newMenuItems = mutableListOf<IListMenuItem>()
                newMenuItems.add(MenuItem(
                    "Set Department Budgets") {
                    replaceFragment(
                        SetDepartmentBudgetsFragment()
                    )
                })

                newMenuItems.add(MenuItem(
                    "Set Cost Type Budgets") {
                    replaceFragment(
                        SetCostTypeBudgetsFragment()
                    )
                })

                replaceFragment(
                    NavigationListFragment(
                        "Set Budgets",
                        true,
                        newMenuItems,
                        headerImage= getLogo(),
                    )
                )
             }
        )

        return NavigationListFragment(
            "Accountant Menu",
            true,
            menuItems,
            headerImage= getLogo(),
        )
    }
}

