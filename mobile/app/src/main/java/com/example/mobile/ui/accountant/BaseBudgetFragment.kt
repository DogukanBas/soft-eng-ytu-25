package com.example.mobile.ui.accountant
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment

abstract class BaseBudgetFragment<T> : BaseFragment() {

    protected abstract val titleResId: Int
    protected abstract val labelResId: Int
    protected abstract val showAddButton: Boolean
    protected abstract fun getItemsOne ()
    protected abstract fun observeState()
    protected abstract fun saveInitialBudget(item: T, initialBudget: Double)
    protected abstract fun saveRemainingBudget(item: T, remainingBudget: Double)

    protected abstract fun resetRemainingBudget(item: T)
    protected abstract var items: MutableList<T>
    protected abstract fun getItemName(item: T): String

    protected abstract fun updateUIForSelectedItem(item: T)
    protected abstract fun updateItemInList(item:T)
    protected abstract fun updateItemBudget(item: T, initial: Double?, remaining: Double?, maxCost : Double? = null): T

    // Views
    protected lateinit var spinnerItem: Spinner
    protected lateinit var etInitialBudgetValue: EditText
    protected lateinit var etRemainingBudgetValue: EditText
    protected lateinit var btnAddItem: Button
    protected lateinit var btnSetInitialBudget: Button
    protected lateinit var btnSetRemainingBudget: Button
    protected lateinit var btnResetToInitial: Button
    protected lateinit var tvTitle: TextView
    protected lateinit var tvItemLabel: TextView
    protected lateinit var etmaxCostValue: EditText
    protected lateinit var btnSetmaxCost: Button
    protected lateinit var tvMaxCost: TextView
    protected lateinit var adapter: ArrayAdapter<String>
    protected var selectedItem: T? = null
    protected var itemNames = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.set_budget, container, false)

        spinnerItem = view.findViewById(R.id.spinner_item)
        etInitialBudgetValue = view.findViewById(R.id.et_initial_budget_value)
        etRemainingBudgetValue = view.findViewById(R.id.et_remaining_budget_value)
        btnAddItem = view.findViewById(R.id.btn_add_item)
        btnSetInitialBudget = view.findViewById(R.id.btn_set_initial_budget)
        btnSetRemainingBudget = view.findViewById(R.id.btn_set_remaining_budget)
        btnResetToInitial = view.findViewById(R.id.btn_reset_to_initial)
        tvTitle = view.findViewById(R.id.tv_title)
        tvItemLabel = view.findViewById(R.id.tv_item_label)
        etmaxCostValue = view.findViewById(R.id.et_max_budget_value)
        btnSetmaxCost = view.findViewById(R.id.btn_set_max_budget_value)
        tvMaxCost = view.findViewById(R.id.tv_max_budget_label)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle.text = getString(titleResId)
        tvItemLabel.text = getString(labelResId)
        btnAddItem.visibility = if (showAddButton) View.VISIBLE else View.GONE
        btnSetmaxCost.visibility =if (showAddButton) View.VISIBLE else View.GONE
        etmaxCostValue.visibility = if (showAddButton) View.VISIBLE else View.GONE
        tvMaxCost.visibility = if (showAddButton) View.VISIBLE else View.GONE


        setupListeners()
        observeState() //TODO even on error fragment shows for a second,
        getItemsOne()
        Log.i(TAG, "getItems() called")
    }

    protected fun setupSpinner() {
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, this.items.map{ getItemName(it) })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerItem.adapter = adapter

        spinnerItem.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedItem = items[position]
                updateUIForSelectedItem(items[position])
                Log.i(TAG,"items is $items")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                etInitialBudgetValue.text.clear()
                etRemainingBudgetValue.text.clear()
                etmaxCostValue.text.clear()
            }
        }
    }

    private fun setupListeners() {
        btnSetInitialBudget.setOnClickListener {
            val input = etInitialBudgetValue.text.toString().replace(",", ".")
            val item = selectedItem ?: return@setOnClickListener
            input.toDoubleOrNull()?.let { newValue ->
                selectedItem = updateItemBudget(item, initial = newValue, remaining = null)
                saveInitialBudget(item, newValue)
                etInitialBudgetValue.text.clear()
            } ?: run {
                etInitialBudgetValue.error = "Invalid number"
            }
        }

        btnSetRemainingBudget.setOnClickListener {
            val input = etRemainingBudgetValue.text.toString().replace(",", ".")
            val item = selectedItem ?: return@setOnClickListener
            input.toDoubleOrNull()?.let { newValue ->
                selectedItem = updateItemBudget(item, initial = null, remaining = newValue)
                saveRemainingBudget(item, newValue)
                etRemainingBudgetValue.text.clear()
            } ?: run {
                etRemainingBudgetValue.error = "Invalid number"
            }
        }

        btnResetToInitial.setOnClickListener {
            val item = selectedItem ?: return@setOnClickListener
            selectedItem = updateItemBudget(item, initial = null, remaining = etInitialBudgetValue.hint.toString().replace(",", ".").toDouble())
            resetRemainingBudget(item)
        }
    }
}
