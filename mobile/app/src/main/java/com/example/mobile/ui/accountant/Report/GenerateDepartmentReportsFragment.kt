package com.example.mobile.ui.accountant.Report

import GenerateReportFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.CustomInput.InputValidator
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class GenerateDepartmentReportsFragment:BaseFragment() {
    private lateinit var inputList: MutableList<CustomInputFormat>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_input_list_layout, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        arrangeList()
        val menuFragment = setMenu()

        childFragmentManager.beginTransaction()
            .replace(R.id.input_menu_container, menuFragment)
            .commit()
    }
    private fun arrangeList() {
        inputList.add(
            CustomInputFormat(
                "Enter Start Date",
                EditTextInputType.Date,
                null,
                "Enter valid date",
                InputValidator { customInputFormat: CustomInputFormat ->
                    try {
                        if (customInputFormat.text.isEmpty()) return@InputValidator false
                        val array = customInputFormat.text.split("/").toTypedArray()
                        val date = array[2].substring(2) + array[1] + array[0]
                        val now = Calendar.getInstance().time
                        val sdf = SimpleDateFormat("yyMMdd")
                        sdf.format(now).toInt() >= date.toInt()
                    } catch (_: Exception) {
                        false
                    }
                }
            ).apply {
                maxDate = Date().time // set max date to today
            }
        )
        inputList.add(
            CustomInputFormat(
                "Enter End Date",
                EditTextInputType.Date,
                null,
                "Enter valid date",
                InputValidator { customInputFormat: CustomInputFormat ->
                    try {
                        if (customInputFormat.text.isEmpty()) return@InputValidator false
                        val array = customInputFormat.text.split("/").toTypedArray()
                        val date = array[2].substring(2) + array[1] + array[0]
                        val now = Calendar.getInstance().time
                        val sdf = SimpleDateFormat("yyMMdd")
                        sdf.format(now).toInt() >= date.toInt()
                    } catch (_: Exception) {
                        false
                    }
                }
            ).apply {
                maxDate = Date().time // set max date to today
            }
        )
    }
    private fun setMenu( ):InputMenuFragment330{
        return InputMenuFragment330(inputList, { outputList ->

            val array = outputList[0].toString().split("/").toTypedArray()
            val date = array[0].substring(2) + array[1] + array[0]
            val formatter = DateTimeFormatter.ofPattern("yyMMdd")
            val updatedDate = LocalDate.parse(date, formatter)

            val endArray = outputList[1].toString().split("/").toTypedArray()
            val endDate = endArray[0].substring(2) + endArray[1] + endArray[0]
            val updatedEndDate = LocalDate.parse(endDate, formatter)

            replaceFragment(
                GenerateReportFragment(
                    listOf("updatedDate","updatedEndDate"),
                    listOf(1.0f,2.0f)
                )
            )
        }, "Create com.example.mobile.model.Ticket.Ticket")
    }

}