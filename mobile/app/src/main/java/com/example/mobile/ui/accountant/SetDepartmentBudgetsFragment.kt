package com.example.mobile.ui.accountant

import android.os.Bundle
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
    override var items = emptyList<DepartmentBudgetResponse>()

    private val viewModel: DepartmentViewModel by viewModels()

    override fun getItemsOne() {
        viewModel.getDepartmentsWithBudget()
    }

    override fun observeState (){
        observeUiState(
            viewModel.departmentBudgetState,
            onSuccess = { data ->
                setupSpinner(data)
            }
        )
    }

    override fun getItemName(item: DepartmentBudgetResponse): String = item.name

    override fun updateUIForSelectedItem(item: DepartmentBudgetResponse) {
        etInitialBudgetValue.hint = "%.2f".format(item.initialBudget)
        etRemainingBudgetValue.hint = "%.2f".format(item.remainingBudget)
    }
    override fun updateUIForReset() {
        etRemainingBudgetValue.hint = etInitialBudgetValue.hint

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
    ): DepartmentBudgetResponse {
        return item.copy(
            initialBudget = initial ?: item.initialBudget,
            remainingBudget = remaining ?: item.remainingBudget
        )
    }
}
