package com.example.mobile.ui.accountant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.utils.DialogType
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCostTypeItemFragment: BaseFragment() {
    private val viewModel: CostTypeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inputList :List<CustomInputFormat> = listOf(
            CustomInputFormat(
                "Name Of New Cost Type",
                EditTextInputType.Text,
                100,
                true,
                "Cost Type Name can't Be Empty",
                {input->
                    input.text.isNotEmpty()
                }
            ),
            CustomInputFormat(
                "Initial Budget",
                EditTextInputType.Number,
                15,
                false,
                "Budget Can't Be Empty",
                {input->
                    input.text.isNotBlank() && input.text.toDouble() > 0



                }
            ),
            CustomInputFormat(
                "Max Cost",
                EditTextInputType.Number,
                15,
                false,
                "Enter a positive value",
                {input->
                    input.text.isNotBlank() && input.text.toDouble() > 0

                }
            )
        )
        setStateCollectors()
        val addCostTypeFragment = InputMenuFragment330(inputList,{outputList->
            Log.i(TAG,"Add Cost Type button triggered")
            val costTypeName = outputList.get(0).toString()
            val initialBudget = outputList.get(1).toString().toDouble()
            val maxCost = outputList.get(2).toString().toDouble()
            viewModel.addCostTypeBudget(costTypeName, initialBudget,maxCost)
        },
            "Add Cost Type"
        )

        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, addCostTypeFragment)
            .commit()

        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }

    private fun setStateCollectors() {
        val dialog = getDialog(DialogType.LOADING,"Loading")
        Log.i(TAG, "setStateCollectors: ")
        observeUiState(
            viewModel.addCostTypeBudgetState,
            onSuccess = { data ->
                Log.i(TAG, "Success: $data")
                dialog.dismiss()
                getDialog(DialogType.SUCCESS,"Cost Item added successfully")
                    .show(requireActivity().supportFragmentManager, "SuccessDialog")
                popFragment()
            },
            onError = { message ->
                Log.e(TAG, "Error: $message")
                dialog.dismiss()
                getDialog(DialogType.ERROR,message).show(requireActivity().supportFragmentManager, "ErrorDialog")
            },
            onLoading = {
                Log.i(TAG, "Loading")
                dialog.show(
                    requireActivity().supportFragmentManager,
                    "ProcessingDialog"
                )
            },
        )
    }

}