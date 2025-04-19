package com.example.mobile.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.utils.UiState
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import com.token.uicomponents.infodialog.InfoDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddDepartmentFragment : Fragment() {
    private val viewModel: AdminViewModel by viewModels()


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
            Log.i("AddDepartment","Add Department buton triggered")
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
        val dialog = InfoDialog.newInstance(InfoDialog.InfoType.Processing, "Department is being added", false)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addDepartmentState.collect { state ->
                    when (state) {
                        is UiState.Idle -> {
                            Log.i("AddDepartmentFragment", "State: Idle")
                        }

                        is UiState.Loading -> {
                            Log.i("AddDepartmentFragment", "State: Loading")
                            dialog.show(
                                requireActivity().supportFragmentManager,
                                "ProcessingDialog"
                            )
                        }

                        is UiState.Success -> {
                            Log.i(
                                "AddDepartmentFragment",
                                "State: Success with data: ${state.data}"
                            )
                            dialog.dismiss()
                            InfoDialog.newInstance(
                                InfoDialog.InfoType.Confirmed,
                                "Department added successfully",
                                true
                            )
                                .show(requireActivity().supportFragmentManager, "SuccessDialog")
                            (activity as MainActivity).popFragment()
                        }

                        is UiState.Error -> {
                            Log.e(
                                "AddDepartmentFragment",
                                "State: Error with message: ${state.message}"
                            )
                            dialog.dismiss()
                            InfoDialog.newInstance(InfoDialog.InfoType.Error, state.message, true)
                                .show(requireActivity().supportFragmentManager, "ErrorDialog")
                        }
                    }
                }
            }
        }
    }
}