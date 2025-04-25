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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.remote.dtos.auth.DepartmentBudgetResponse
import com.example.mobile.remote.dtos.auth.DepartmentBudgetResponseList
import com.example.mobile.ui.BaseFragment
import com.example.mobile.ui.admin.AdminMenuFragment.Companion.TAG
import com.example.mobile.ui.admin.AdminViewModel
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SetDepartmentBudgetsFragment : BaseFragment() {
    private val departmentViewModel: DepartmentViewModel by viewModels()
    private var departments: List<DepartmentBudgetResponse> = emptyList()

    // View references
    private lateinit var spinnerDepartment: Spinner
    private lateinit var etInitialBudgetValue: EditText
    private lateinit var etRemainingBudgetValue: EditText
    private lateinit var btnSetInitialBudget: Button
    private lateinit var btnSetRemainingBudget: Button
    private lateinit var btnResetToInitial: Button

    // Store original values
    private var originalInitialBudget: String = "$0.00"
    private var originalRemainingBudget: String = "$0.00"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.set_department_budget, container, false)

        // Initialize views
        spinnerDepartment = view.findViewById(R.id.spinner_department)
        etInitialBudgetValue = view.findViewById(R.id.et_initial_budget_value)
        etRemainingBudgetValue = view.findViewById(R.id.et_remaining_budget_value)
        btnSetInitialBudget = view.findViewById(R.id.btn_set_initial_budget)
        btnSetRemainingBudget = view.findViewById(R.id.btn_set_remaining_budget)
        btnResetToInitial = view.findViewById(R.id.btn_reset_to_initial)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setupButtons()
    }





    private fun init() {
        departmentViewModel.getDepartmentsWithBudget()

        viewLifecycleOwner.lifecycleScope.launch {
            departmentViewModel.departmentBudgetState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        Log.i(TAG, "Departments: ${state.data}")
                        setupDepartmentSpinner(state.data)
                    }
                    is UiState.Error -> {
//                        (activity as MainActivity).popFragment()
//                        (activity as MainActivity).getDialog(
//                            DialogType.ERROR,
//                            "Can't retrieve departments: ${state.message}"
//                        ).show(childFragmentManager, "ErrorDialog")
                    }
                    UiState.Loading -> {
                        // Show loading indicator if needed
                    }
                    UiState.Idle -> {
                        // Initial state, do nothing
                    }
                }
            }
        }
    }

    private fun setupDepartmentSpinner(departments: List<DepartmentBudgetResponse>) {
        // Setup department spinner with sample departments
        //val departments = arrayOf("HR", "IT", "Finance", "Marketing", "Operations")
        //iterate over DepartmentBudgetresponselist, get each elements deptname as departments to be listed
        this.departments = departments
        val departmentNames = departments.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departmentNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDepartment.adapter = adapter

        // Listen for department selection changes
        spinnerDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                    updateUIForSelectedDepartment()


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Clear fields when nothing is selected
                etInitialBudgetValue.text.clear()
                etRemainingBudgetValue.text.clear()
            }
        }
    }

    private fun setupButtons() {
        // Set Initial Budget Button
        btnSetInitialBudget.setOnClickListener {
            val departmentName = spinnerDepartment.selectedItem.toString()
            val newBudgetText = etInitialBudgetValue.text.toString()

            if (newBudgetText.isNotEmpty()) {
                val newBudget = newBudgetText.toDouble()

                departments = departments.map {
                    if (it.name == departmentName)
                        it.copy(initialBudget = newBudget)
                    else
                        it
                }
                updateUIForSelectedDepartment()
                //clear initial budget edit text
                etInitialBudgetValue.text.clear()
                saveInitialBudget(departmentName, newBudgetText)
                originalInitialBudget = newBudgetText
                etInitialBudgetValue.hint = "%.2f".format(newBudget)
                Toast.makeText(context, "Initial Budget set to $newBudgetText", Toast.LENGTH_SHORT).show()
            } else {
                etInitialBudgetValue.error = "Please enter a value"
            }
        }


        // Set Remaining Budget Button
        btnSetRemainingBudget.setOnClickListener {
            val departmentName = spinnerDepartment.selectedItem.toString()
            val newBudget = etRemainingBudgetValue.text.toString()

            if (newBudget.isNotEmpty()) {
                saveRemainingBudget(departmentName, newBudget)
                departments = departments.map {
                    if (it.name == departmentName)
                        it.copy(remainingBudget = newBudget.toDouble())
                    else
                        it
                }
                updateUIForSelectedDepartment()
                etRemainingBudgetValue.text.clear()
                originalRemainingBudget = newBudget
                etRemainingBudgetValue.hint = newBudget

                Toast.makeText(context, "Remaining Budget set to $newBudget", Toast.LENGTH_SHORT).show()
            } else {
                etRemainingBudgetValue.error = "Please enter a value"
            }
        }

        // Reset Remaining to Initial Button
        btnResetToInitial.setOnClickListener {
            val department = spinnerDepartment.selectedItem.toString()
            resetRemainingBudgetToInitial(department)
            departments = departments.map {
                if (it.name == department)
                    it.copy(remainingBudget = originalInitialBudget.toDouble())
                else
                    it
            }
            // clear the remaining budget field
            updateUIForSelectedDepartment()
        }
    }



    private fun saveInitialBudget(department: String, budget: String) {
        //budget to double
        val budgetDouble = budget.toDoubleOrNull()

        departmentViewModel.setInitialBudget(department, budgetDouble!!)

    }

    private fun saveRemainingBudget(department: String, budget: String) {
        //budget to double
        val budgetDouble = budget.toDoubleOrNull()
        departmentViewModel.setRemainingBudget(department, budgetDouble!!)
        // TODO: Save the remaining budget to your data source
    }

    private fun resetRemainingBudgetToInitial(deptname: String) {
        // Reset remaining budget to initial budget value

        departmentViewModel.resetRemainingBudget(deptname)
        Toast.makeText(context, "Remaining Budget reset to Initial Budget", Toast.LENGTH_SHORT).show()
    }
    private fun updateUIForSelectedDepartment() {
        val selectedDepartmentName = spinnerDepartment.selectedItem.toString()
        val selectedDepartment = departments.find { it.name == selectedDepartmentName }

        selectedDepartment?.let { department ->
            etInitialBudgetValue.hint = "%.2f".format(department.initialBudget)
            etRemainingBudgetValue.hint = "%.2f".format(department.remainingBudget)

            originalInitialBudget = department.initialBudget.toString()
            originalRemainingBudget = department.remainingBudget.toString()
        }
    }

}