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
import com.example.mobile.model.User.Employee
import com.example.mobile.model.User.UserType
import com.example.mobile.utils.UiState
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import com.token.uicomponents.infodialog.InfoDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddUserFragment : Fragment() {
    private val viewModel: AdminViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val arrangedInputList = arrangeInputList()
        Log.i("AddUserFragment", "Input list: $arrangedInputList")
        setStateCollectors()
        val addUseInputFragment = InputMenuFragment330(arrangedInputList,  buttonListener = { inputList ->
            Log.d("InputMenuFragment330", "inputList: $inputList")
            val userType :String = inputList?.get(0).toString()
            val name :String = inputList?.get(1).toString()
            val surname :String = inputList?.get(2).toString()
            val password:String = inputList?.get(3).toString()

            val email :String = inputList?.get(4).toString()
            val personalNo :String = inputList?.get(5).toString()
            val department :String = inputList?.get(6).toString()
            val employee = Employee(UserType.fromString(userType), name, surname, password, email, personalNo, department)
            viewModel.addUser(employee)
            Log.i("AddUserFragment", "userType: $userType, name: $name, surname: $surname, email: $email, personalNo: $personalNo,password: $password, department: $department")
        },
            btnOKName = "Kullanıcı Oluştur",)

        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, addUseInputFragment)
            .commit()

        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }

    private fun setStateCollectors() {
        Log.i("AddUserFragment", "Setting up state collectors")

        val dialog = InfoDialog.newInstance(InfoDialog.InfoType.Processing, "User is being added", false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addUserState.collect { state ->
                    Log.i("AddUserFragment", "Received state: $state")

                    when (state) {
                        is UiState.Idle -> {
                            Log.i("AddUserFragment", "State: Idle")
                        }
                        is UiState.Loading -> {
                            Log.i("AddUserFragment", "State: Loading")
                            dialog.show(requireActivity().supportFragmentManager, "ProcessingDialog")
                        }
                        is UiState.Success -> {
                            Log.i("AddUserFragment", "State: Success with data: ${state.data}")
                            dialog.dismiss()
                            InfoDialog.newInstance(InfoDialog.InfoType.Confirmed, "User added successfully", true)
                                .show(requireActivity().supportFragmentManager, "SuccessDialog")
                            (activity as MainActivity).popFragment()
                        }
                        is UiState.Error -> {
                            Log.e("AddUserFragment", "State: Error with message: ${state.message}")
                            dialog.dismiss()
                            InfoDialog.newInstance(InfoDialog.InfoType.Error, state.message, true)
                                .show(requireActivity().supportFragmentManager, "ErrorDialog")
                        }
                    }
                }
            }
        }
    }
    private fun arrangeInputList() : MutableList<CustomInputFormat> {
        val inputList = mutableListOf<CustomInputFormat>()
        //setStateCollectors()
        Log.i("AddUserFragment", "Current addUserState value: ${viewModel.addUserState.value}")
        inputList.add(CustomInputFormat("Select Account Type", EditTextInputType.MenuList,null,null,null).apply {
            singleSelectionListItems = listOf(
                "Team Member",
                "Manager",
                "Accountant"
            )
            text = singleSelectionListItems[0]

        })

        inputList.add(
            CustomInputFormat("Name",
                EditTextInputType.Text,100,"Name can't be empty",  { input: CustomInputFormat ->
                input.text.toString().isNotEmpty()
            })
        )
        inputList.add(
            CustomInputFormat("Surname",
                EditTextInputType.Text,100,"Surname can't be empty",  { input: CustomInputFormat ->
                input.text.toString().isNotEmpty()
            })
        )
        inputList.add(
            CustomInputFormat("Password",
                EditTextInputType.Password, 20,"Password can't be empty ",  { input: CustomInputFormat ->
                input.text.toString().isNotEmpty()
            })
        )
        inputList.add(
            CustomInputFormat("Email",
                EditTextInputType.Text,255,"Invalid Email",  { input: CustomInputFormat ->
            input.text.toString().isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(input.text.toString()).matches()
        })
        )


        inputList.add(
            CustomInputFormat("Personal No",
                EditTextInputType.Number, 20,"Personal No can't be empty ",  { input: CustomInputFormat ->
                input.text.toString().isNotEmpty()
            })
        )
        inputList.add(CustomInputFormat("Department", EditTextInputType.MenuList,null,null,null).apply {
            singleSelectionListItems = listOf(
                "HR",
                "IT",
                "Finance"
            )
            text= singleSelectionListItems[0]

        })
        return inputList
    }


}