package com.example.mobile.ui.accountant

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.remote.dtos.auth.CostTypeBudgetResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetCostTypeBudgetsFragment : BaseBudgetFragment<CostTypeBudgetResponse>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAddItem.setOnClickListener {
            replaceFragment(
                AddCostTypeItemFragment(), true
            )
        }
    }
    override fun getItemsOne () {
        viewModel.getCostTypesWithBudget()
    }


    override val titleResId = R.string.set_cost_type_budget_title
    override val labelResId = R.string.cost_type_label
    override val showAddButton = true
    override var items = emptyList<CostTypeBudgetResponse>()


    private val viewModel: CostTypeViewModel by viewModels()


    override fun observeState (){
        observeUiState(
            viewModel.costTypeBudgetState,
            onSuccess = {data->
            setupSpinner(data)
        }
        )
    }



    override fun getItemName(item: CostTypeBudgetResponse): String {
       return item.name
    }
    override fun updateUIForSelectedItem(item: CostTypeBudgetResponse) {
        etInitialBudgetValue.hint = "%.2f".format(item.initialBudget)
        etRemainingBudgetValue.hint = "%.2f".format(item.remainingBudget)
    }
    override fun updateUIForReset() {
        etRemainingBudgetValue.hint = etInitialBudgetValue.hint
    }

    override fun saveInitialBudget(item: CostTypeBudgetResponse, initialBudget: Double) {
        viewModel.setInitialBudget(item.name, initialBudget)

    }
    override fun saveRemainingBudget(item: CostTypeBudgetResponse, remainingBudget: Double) {
        viewModel.setRemainingBudget(item.name, remainingBudget)
    }

    override fun resetRemainingBudget(item: CostTypeBudgetResponse) {
        viewModel.resetRemainingBudget(item.name)
        Toast.makeText(context, "Remaining budget reset to initial budget", Toast.LENGTH_SHORT).show()
    }
    override fun updateItemBudget(item: CostTypeBudgetResponse, initial: Double?, remaining: Double?): CostTypeBudgetResponse {
        return item.copy(initialBudget = initial ?: item.initialBudget, remainingBudget = remaining ?: item.remainingBudget)
    }



}