package com.example.mobile.ui.accountant

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.remote.dtos.auth.CostTypeBudgetResponse
import com.example.mobile.remote.dtos.auth.DepartmentBudgetResponse
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
        btnSetmaxCost.setOnClickListener{
            val input = etmaxCostValue.text.toString().replace(",", ".")
            val item = selectedItem ?: return@setOnClickListener
            input.toDoubleOrNull()?.let { newValue ->
                selectedItem = updateItemBudget(item, initial = null, remaining = null , maxCost = newValue)
                viewModel.setmaxCost(item.name, newValue)
                etmaxCostValue.text.clear()
            } ?: run {
                etmaxCostValue.error = "Invalid number"
            }
        }
    }
    override fun getItemsOne () {
        viewModel.getCostTypesWithBudget()
    }


    override val titleResId = R.string.set_cost_type_budget_title
    override val labelResId = R.string.cost_type_label
    override val showAddButton = true
    override var items : MutableList<CostTypeBudgetResponse> = mutableListOf()


    private val viewModel: CostTypeViewModel by viewModels()


    override fun observeState (){
        observeUiState(
            viewModel.costTypeBudgetState,
            onSuccess = {data->
                //assign the data to the items list
                this.items = data.toMutableList()
                setupSpinner()

            },
            onError = {
                Log.e(TAG, "Error fetching cost types: $it")
                popFragment()
                Toast.makeText(context, "Error fetching cost types: $it", Toast.LENGTH_SHORT).show()
            }
        )

        observeUiState(
            viewModel.addCostTypeBudgetState,
            onSuccess = {
                Toast.makeText(context, "Budget updated successfully", Toast.LENGTH_SHORT).show()
                Log.i("Curr selected item", "selected item is $selectedItem")
                updateItemInList(selectedItem!!)
                updateUIForSelectedItem(selectedItem!!)
                Toast.makeText(context, "Budget updated successfully", Toast.LENGTH_SHORT).show()
            },
            onError = {
                etInitialBudgetValue.text.clear()
                Toast.makeText(context, "Error updating budget: $it", Toast.LENGTH_SHORT).show()
            }

        )
    }



    override fun getItemName(item: CostTypeBudgetResponse): String {
       return item.name
    }
    override fun updateUIForSelectedItem(item: CostTypeBudgetResponse) {


        etInitialBudgetValue.hint = "%.2f".format(item.initialBudget)
        etRemainingBudgetValue.hint = "%.2f".format(item.remainingBudget)
        etmaxCostValue.hint = "%.2f".format(item.maxCost)
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
    override fun updateItemBudget(item: CostTypeBudgetResponse, initial: Double?, remaining: Double?, maxCost:Double?): CostTypeBudgetResponse {
        return item.copy(initialBudget = initial ?: item.initialBudget, remainingBudget = remaining ?: item.remainingBudget, maxCost = maxCost?: item.maxCost)
    }
    override fun updateItemInList(updatedItem: CostTypeBudgetResponse) {
        val index = items.indexOfFirst { it.name == updatedItem.name }
        this.items[index] = updatedItem
        Log.i(TAG, "Item updated:current items $items")
        adapter.notifyDataSetChanged()
    }



}