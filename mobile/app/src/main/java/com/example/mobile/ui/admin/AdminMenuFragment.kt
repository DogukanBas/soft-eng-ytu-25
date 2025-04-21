package com.example.mobile.ui.admin

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
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.MenuItem
import com.example.mobile.utils.UiState
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminMenuFragment : Fragment() {

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
        setStateObservers()
        val menuFragment = setMenu()

        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, menuFragment)
            .commit()
    }

    private fun getLogo(): Int {
        return R.drawable.ic_launcher_foreground
    }

    private fun setStateObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getDepartmentsState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        Log.i(TAG, "Success: ${state.data}")
                        val departments = state.data
                        (activity as MainActivity).replaceFragment(
                            AddUserFragment(departments)
                        )

                    }
                    is UiState.Error -> {
                        // Handle error state
                        (activity as MainActivity).getDialog(
                            DialogType.ERROR,
                            "Can't retrieve departments"
                        ).show(
                            childFragmentManager,
                            "ErrorDialog"
                        )

                    }
                    else -> {}
                }
            }
        }
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
                (activity as MainActivity).replaceFragment(AddDepartmentFragment()) }
        ))
        return NavigationListFragment(
            "Admin Menu",
            true,
            menuItems,
            headerImage= getLogo(),
        )
    }
}

