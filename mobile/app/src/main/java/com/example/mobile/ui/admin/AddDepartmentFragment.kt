package com.example.mobile.ui.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.UiState
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddDepartmentFragment : BaseFragment() {
    private val viewModel: AdminViewModel by viewModels()
    companion object{
        val TAG = "AddDepartmentFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inputList :List<CustomInputFormat> = listOf(
            CustomInputFormat(
                "Yeni Departmanın Adını Giriniz",
                EditTextInputType.Text,
                100,
                true,
                "Boş Bırakılamaz",
                {input->
                    input.text.isNotEmpty()
                }


            ).apply{
                realHint = "Departman Adı"
            },
        )
        setStateCollectors()
        val addDepartmentFragment = InputMenuFragment330(inputList,{outputList->
            Log.i(TAG,"Add Department button triggered")
            val departmentName = outputList.get(0).toString()
            viewModel.addDepartment(departmentName)
        },
            "Departman Oluştur"
        )

        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, addDepartmentFragment)
            .commit()

        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }
    //todo this reccurs, make this generic in a util maybe
    private fun setStateCollectors() {
        val dialog = getDialog(DialogType.LOADING,"Loading")
        observeUiState(
            viewModel.addDepartmentState,
            onSuccess = { data ->
                Log.i(TAG, "Success: $data")
                dialog.dismiss()
                getDialog(DialogType.SUCCESS,"Department added successfully")
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