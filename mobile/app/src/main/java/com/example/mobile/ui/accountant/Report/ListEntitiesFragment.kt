package com.example.mobile.ui.accountant.Report

import GenerateReportFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.remote.dtos.Report.EntityResponse
import com.example.mobile.ui.BaseFragment
import com.example.mobile.utils.MenuItem
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListEntitiesFragment(
    private val type:String,
    private val data:List<EntityResponse>,
    private val title:String

): BaseFragment() {
    private val viewModel : ReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStateObservers()
        val menuFragment = setMenu()
        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, menuFragment)
            .commit()
    }
    private fun setStateObservers(){

        observeUiState(
            viewModel.getReportUiState,
            onSuccess = {data ->
                Log.i("ON success for get report", data.toString())
                val xValues :List<String > = data.map { it.date }
                val yValues :List<Float > = data.map { it.amount }
                viewModel.resetReportUiState()

                replaceFragment(
                    GenerateReportFragment(
                        xValues,
                        yValues

                    )
                )

            },
            onError = {
                Log.i("ON error for get report", it)
                viewModel.resetReportUiState()

                showError(it)
            }
        )
    }
    private fun setMenu(): NavigationListFragment {
        val menuItems = mutableListOf<IListMenuItem>()
        data.forEach {
            val id = it.id
            menuItems.add(MenuItem(
                it.name

            ) {

                viewModel.getReport(type,id)
            })
        }

        return NavigationListFragment(
            title,
            true,
            menuItems

        )
    }

}