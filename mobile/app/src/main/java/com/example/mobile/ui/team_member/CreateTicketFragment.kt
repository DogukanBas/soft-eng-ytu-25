package com.example.mobile.ui.accountant

import Ticket
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
import com.example.mobile.ui.team_member.TeamMemberViewModel
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.MenuItem
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.CustomInput.InputListFragment
import com.token.uicomponents.CustomInput.InputValidator
import com.token.uicomponents.ListMenuFragment.IListMenuItem
import com.token.uicomponents.components330.input_menu_fragment.BtnOkVisibilityModes
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import com.token.uicomponents.components330.navigation_list_fragment.NavigationListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.Calendar

@AndroidEntryPoint
class CreateTicketFragment ( private val costTypes: List<String>): BaseFragment() {

    companion object {
        val TAG = "CreateTicketFragment"
    }

    private val viewModel: TeamMemberViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        setStateCollectors()
        val menuFragment = setMenu()
        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, menuFragment )
            .commit()
    }

    private fun setStateCollectors() {
        Log.i(TAG, "Setting up state collectors")
        val dialog = getDialog(DialogType.LOADING,"Loading")

        observeUiState(
            viewModel.createTicketState,
            onSuccess = { data ->
                Log.i(TAG, "Success: $data")
                dialog.dismiss()
                getDialog(DialogType.SUCCESS,"Ticket Created Successfully")
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
    private fun getLogo(): Int {
        return R.drawable.ic_launcher_foreground
    }

    private fun setMenu(): InputListFragment {
        val inputList: MutableList<CustomInputFormat> = mutableListOf()
        inputList.add(CustomInputFormat("Select Account Type", EditTextInputType.MenuList,null,null,
            { input -> input.text.isNotEmpty() }).apply {
            singleSelectionListItems = costTypes
            text = singleSelectionListItems[0]
        })
        inputList.add(CustomInputFormat(
            "Enter Amount",
            EditTextInputType.Number,
            100,
            true,
            "Boş Bırakılamaz",
            { input ->
                input.text.isNotEmpty() && input.text.toDoubleOrNull() != null
            }
        ))
        inputList.add(CustomInputFormat(
            "Enter Date",
            EditTextInputType.Date,
            null,
            "null",
            InputValidator { customInputFormat: CustomInputFormat ->
                try {
                    val array = customInputFormat.text.split("/").toTypedArray()
                    val date = array[2].substring(2) + array[1] + array[0]
                    val now = Calendar.getInstance().time
                    val sdf = SimpleDateFormat("yyMMdd")
                    return@InputValidator sdf.format(now).toInt() >= date.toInt()
                } catch (_: Exception) {
                }
                false
            }

        ))

        inputList.add(CustomInputFormat(
            "Enter Description",
            EditTextInputType.Text,
            100,
            true,
            "Boş Bırakılamaz",
            { input ->
                input.text.isNotEmpty()
            }
        ).apply {
            realHint = "Description"
        })
        inputList.add(CustomInputFormat(
            "Enter Invoice String",
            EditTextInputType.Text,
            100,
            true,
            "Boş Bırakılamaz",
            { input ->
                input.text.isNotEmpty()
            }
        ).apply {
            realHint = "Invoice String"
        })



        return InputMenuFragment330(inputList,  { outputList ->
            Log.i(TAG, "Create Ticket button triggered")
            val costType = outputList[0].toString()
            val amount = outputList[1].toString().toDoubleOrNull()

            val array = outputList[2].toString().split("/").toTypedArray()
            val date = array[2].substring(2) + array[1] + array[0]
            val description = outputList[3].toString()
            val invoiceString = outputList[4].toString()

            val formatter = DateTimeFormatter.ofPattern("yyMMdd") // Adjust pattern based on your date format
            val updatedDate =  LocalDate.parse(date, formatter)

            Log.i(TAG, "All data: $costType, $amount, $date and updated date = ${updatedDate::class.simpleName}, $description, $invoiceString")
            Log.i(TAG, "Ticket created successfully")
            val ticket = Ticket(
                costType = costType,
                amount = amount ?: 0.0,
                description = description,
                date = updatedDate.toString(),
                invoice =  Base64.getEncoder().encodeToString(invoiceString.toByteArray(Charsets.UTF_8))

            )
            viewModel.createTicket(ticket)


        }, "Create Ticket")


    }
}


