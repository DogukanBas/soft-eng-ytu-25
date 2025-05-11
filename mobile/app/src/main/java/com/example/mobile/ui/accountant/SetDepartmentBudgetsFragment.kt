package com.example.mobile.ui.accountant

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.remote.dtos.auth.DepartmentBudgetResponse

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SetDepartmentBudgetsFragment : BaseBudgetFragment<DepartmentBudgetResponse>() {
    override val titleResId = R.string.set_department_budget_title
    override val labelResId = R.string.department_label
    override val showAddButton = false
    override var items = mutableListOf<DepartmentBudgetResponse>()

    private val viewModel: DepartmentViewModel by viewModels()

    override fun getItemsOne() {
        viewModel.getDepartmentsWithBudget()
    }

    override fun observeState (){
        observeUiState(
            viewModel.departmentBudgetState,
            onSuccess = { data ->
                this.items = data.toMutableList()
                setupSpinner()
            },
            onError = {
                Log.e(TAG, "Error fetching departments: $it")
                popFragment()
            }

        )
        observeUiState(
            viewModel.addDepartmentBudgetState,
            onSuccess = {
                updateItemInList(selectedItem!!)
                updateUIForSelectedItem(selectedItem!!)
                Toast.makeText(context, "Budget updated successfully", Toast.LENGTH_SHORT).show()
            },
            onError = {
                Toast.makeText(context, "Error updating budget: $it", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun getItemName(item: DepartmentBudgetResponse): String = item.name

    override fun updateUIForSelectedItem(item: DepartmentBudgetResponse) {
        etInitialBudgetValue.hint = "%.2f".format(item.initialBudget)
        etRemainingBudgetValue.hint = "%.2f".format(item.remainingBudget)
    }


    override fun saveInitialBudget(item: DepartmentBudgetResponse, value: Double) {
        viewModel.setInitialBudget(item.name, value)
    }

    override fun saveRemainingBudget(item: DepartmentBudgetResponse, value: Double) {
        viewModel.setRemainingBudget(item.name, value)
    }

    override fun resetRemainingBudget(item: DepartmentBudgetResponse) {
        viewModel.resetRemainingBudget(item.name)
        Toast.makeText(context, "Reset remaining to initial", Toast.LENGTH_SHORT).show()
    }

    override fun updateItemBudget(
        item: DepartmentBudgetResponse,
        initial: Double?,
        remaining: Double?
        , maxCost: Double?  // wont be used in this case
    ): DepartmentBudgetResponse {
        return item.copy(
            initialBudget = initial ?: item.initialBudget,
            remainingBudget = remaining ?: item.remainingBudget
        )
    }
    override fun updateItemInList(updatedItem: DepartmentBudgetResponse) {
        val index = items.indexOfFirst { it.name == updatedItem.name }
        this.items[index] = updatedItem
        Log.i(TAG, "Item updated:current items $items")
        adapter.notifyDataSetChanged()
    }
}
