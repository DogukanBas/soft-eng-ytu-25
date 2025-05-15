package com.example.mobile.ui.Report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.ui.accountant.Report.ListEntitiesFragment
import com.example.mobile.utils.MenuItem
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportMenuFragment: BaseFragment() {
    private val viewModel : ReportViewModel by viewModels()
    lateinit var type:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStateObserver()
        val menuFragment = setMenu()

        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, menuFragment)
            .commit()
    }
    private fun setStateObserver() {
      observeUiState(
          viewModel.getEntity,
          onSuccess = {data ->
              Log.i("ON success for get entitiy list", data.toString())
              viewModel.resetEntitityListUiState()

              replaceFragment(
                    ListEntitiesFragment(
                        type,
                        data,
                        type+" Entities",
                    )
                )
          },
            onError = {error ->
                Log.i("ON error for get entitiy list", error.toString())
                viewModel.resetEntitityListUiState()
                showError(error)
            },
      )
    }



    private fun setMenu(): NavigationListFragment{
        val menuItems = mutableListOf<IListMenuItem>()
        menuItems.add(MenuItem(
            "Employee Report") {
            type = "Employee"
            viewModel.getEntities(type)

        })
        menuItems.add(MenuItem(
            "Department Report") {
            type = "Department"
            viewModel.getEntities(type)

        })
        menuItems.add(MenuItem(
            "Cost Type Report") {
            type = "CostType"
            viewModel.getEntities(type)

        })

        return NavigationListFragment(
            "Report Options",
            true,
            menuItems,
        )

    }
}