package com.example.mobile.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.utils.MenuItem
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment

class AdminMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val menuItems = mutableListOf<IListMenuItem>()
        //admin has two functionalities, add user, add department
        menuItems.add(MenuItem(
            "Add User",
            { (activity as MainActivity).replaceFragment(AddUserFragment()) }
        ))
        menuItems.add(MenuItem(
            "Add Department",
            { (activity as MainActivity).replaceFragment(AddDepartmentFragment()) }
        ))
        val menuFragment = NavigationListFragment(
            "Admin Menu",
            true,
            menuItems,
            false,
            "",
            getLogo(),
            null ,
            null ,
            null ,
            null ,
            null ,
            0
        )

        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, menuFragment)
            .commit()
        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }


    private fun getLogo(): Int {
        return R.drawable.ic_launcher_foreground
    }
}

