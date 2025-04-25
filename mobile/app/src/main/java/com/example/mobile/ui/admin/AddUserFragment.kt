package com.example.mobile.ui.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.model.User.Employee
import com.example.mobile.model.User.UserType
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.UiState
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddUserFragment(val ListOfAdmins :List<String> = emptyList()) : BaseFragment() {
    private val viewModel: AdminViewModel by viewModels()
    private lateinit var addUseInputFragment: InputMenuFragment330
    companion object{
        val TAG = "AddUserFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG,"onCreateView")

        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrangedInputList = arrangeInputList()
        Log.i(TAG,"onViewCreated")
        setStateCollectors()
        val addUserInputFragment = setMenu(arrangedInputList)


        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, addUserInputFragment)
            .commit()

    }


    private fun setStateCollectors() {
        Log.i(TAG, "Setting up state collectors")

        val dialog = getDialog(DialogType.LOADING,"Loading")

        observeUiState(
            viewModel.addUserState,
            onSuccess = { data ->
                Log.i(TAG, "Success: $data")
                dialog.dismiss()
                getDialog(DialogType.SUCCESS,"User added successfully")
                    .show(requireActivity().supportFragmentManager, "SuccessDialog")
                popFragment()
            },
            onError = { message ->
                Log.e(TAG, "Error: $message")
                dialog.dismiss()
                getDialog(DialogType.ERROR,message).show(requireActivity().supportFragmentManager, "ErrorDialog")
                popFragment()
            },
            onLoading = {
                Log.i(TAG, "Loading...")
                dialog.show(requireActivity().supportFragmentManager, "ProcessingDialog")
            },
        )

    }
    private fun arrangeInputList() : MutableList<CustomInputFormat> {
        val inputList = mutableListOf<CustomInputFormat>()

        Log.i(TAG, "Current addUserState value: ${viewModel.addUserState.value}")
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
            singleSelectionListItems = ListOfAdmins
            text= singleSelectionListItems[0]
        })
        return inputList
    }
    private fun setMenu(arrangedInputList: MutableList<CustomInputFormat>) : InputMenuFragment330 {
        return  InputMenuFragment330(arrangedInputList,  buttonListener = { inputList ->
            val userType :String = inputList?.get(0).toString()
            val name :String = inputList?.get(1).toString()
            val surname :String = inputList?.get(2).toString()
            val password:String = inputList?.get(3).toString()

            val email :String = inputList?.get(4).toString()
            val personalNo :String = inputList?.get(5).toString()
            val department :String = inputList?.get(6).toString()
            val employee = Employee(UserType.fromString(userType), name, surname, password, email, personalNo, department)
            viewModel.addUser(employee)
            Log.i(TAG, "userType: $userType, name: $name, surname: $surname, email: $email, personalNo: $personalNo,password: $password, department: $department")
        },
            btnOKName = "Kullanıcı Oluştur",)
    }





}