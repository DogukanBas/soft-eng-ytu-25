package com.example.mobile.ui.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.ui.login.LoginFragment
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.MenuItem
import com.example.mobile.utils.UiState
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class AdminMenuFragment : BaseFragment() {

    private val viewModel: AdminViewModel by viewModels()
    companion object {
        val TAG = "AdminMenuFragment"
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
        setAddUserObservers()
        val menuFragment = setMenu()

        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, menuFragment)
            .commit()
    }

    private fun getLogo(): Int {
        return R.drawable.ic_launcher_foreground
    }

    private fun setAddUserObservers(){
        observeUiState(
            viewModel.getDepartmentsState,
            onSuccess = { departments ->
                Log.i(TAG, "Success: $departments")
                if(departments.isEmpty()){
                    getDialog(
                        DialogType.ERROR,
                        "No departments found"
                    ).show(
                        childFragmentManager,
                        "ErrorDialog"
                    )
                }
                else{
                    replaceFragment(
                        AddUserFragment(departments)
                    )
                    viewModel.setGetDepartmentsState(UiState.Idle)
                }

                    },
            onError = {
                getDialog(
                    DialogType.ERROR,
                    "Can't retrieve departments"
                ).show(
                    childFragmentManager,
                    "ErrorDialog"
                )
            }
        )
    }

    private fun setMenu(): NavigationListFragment {
        val menuItems = mutableListOf<IListMenuItem>()
        menuItems.add(MenuItem(
            "Add User"
        ) {
            viewModel.getDepartments()
        })
        menuItems.add(MenuItem(
            "Add Department",
            { Log.i(TAG,"add department init")
               replaceFragment(AddDepartmentFragment()) }
        ))
        menuItems.add(MenuItem(
            "Log Out") {
            Log.i(TAG, "Log Out button clicked")
            replaceFragment(
                LoginFragment(),
                addToBackStack = false
            )
        })
        return NavigationListFragment(
            "Admin Menu",
            true,
            menuItems,
            headerImage= getLogo(),
        )
    }
}

