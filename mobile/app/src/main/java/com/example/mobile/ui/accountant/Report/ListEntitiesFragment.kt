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
class ListEntitiesFragment : BaseFragment() {
    private val viewModel : ReportViewModel by viewModels()

    private var type: String = ""
    private var data: List<EntityResponse> = listOf()
    private var title: String = ""

    companion object {
        private const val ARG_TYPE = "type"
        private const val ARG_DATA = "data"
        private const val ARG_TITLE = "title"

        fun newInstance(type: String, data: ArrayList<EntityResponse>, title: String): ListEntitiesFragment {
            val fragment = ListEntitiesFragment()
            val args = Bundle()
            args.putString(ARG_TYPE, type)
            args.putParcelableArrayList(ARG_DATA, data)
            args.putString(ARG_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            type = args.getString(ARG_TYPE, "")
            data = args.getParcelableArrayList(ARG_DATA) ?: listOf()
            title = args.getString(ARG_TITLE, "")
        }
    }

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
                val xValues :ArrayList<String> = ArrayList(data.map { it.date })
                val yValues :FloatArray = data.map { it.amount }.toFloatArray()
                viewModel.resetReportUiState()

                replaceFragment(
                    GenerateReportFragment.newInstance(
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
                Log.i("ListEntitiesFragment", "Clicked on ${it.name} with id $id")
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