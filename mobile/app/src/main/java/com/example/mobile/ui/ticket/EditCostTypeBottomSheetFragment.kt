package com.example.mobile.ui.ticket

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.adapters.FragmentBottomSheetAdapter
import com.example.mobile.remote.dtos.auth.listticket.EditTicketRequest
import com.example.mobile.ui.BaseFragment
import com.example.mobile.utils.DialogType
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCostTypeBottomSheetFragment (val costTypeList: List<String>, val currentCostType:String, val ticketId:Int): BaseFragment() {
    val viewModel : TicketViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStateCollectors()
        val menu = setMenu()
        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, menu)
            .commit()
    }

    private fun setStateCollectors(){
        val dialog = getDialog(DialogType.LOADING,"Loading")
        observeUiState(
            viewModel.createTicketState,
            onSuccess = { data ->
                Log.i(TAG, "Success: $data")
                dialog.dismiss()

                getDialog(DialogType.SUCCESS,data.message + " \n Afforded amount is  " + data.budget.toString()).show(
                    requireActivity().supportFragmentManager, "SuccessDialog")
                popFragment()
                popFragment()
                (requireParentFragment() as? FragmentBottomSheetAdapter)?.dismiss()
            },
            onError = { message ->
                Log.e(TAG, "Error: $message")
                dialog.dismiss()
                popFragment()
                getDialog(DialogType.ERROR,message).show(requireActivity().supportFragmentManager, "ErrorDialog")
            },
            onLoading = {
                Log.i(TAG, "Loading")
                dialog.show(
                    requireActivity().supportFragmentManager,
                    "ProcessingDialog"
                )
            },
        )}
    private fun setMenu(): InputMenuFragment330 {
        val inputList: MutableList<CustomInputFormat> = mutableListOf()
        inputList.add(CustomInputFormat("Select Cost Type", EditTextInputType.MenuList,null,null,
            { input -> input.text.isNotEmpty() }).apply {
            singleSelectionListItems = costTypeList
            text = currentCostType
        })
        return InputMenuFragment330(inputList,  { outputList ->
            val costType = outputList[0].toString()
            viewModel.editCostTypeTicket(ticketId,costType)


        }, "Edit Cost Type ")
    }


}