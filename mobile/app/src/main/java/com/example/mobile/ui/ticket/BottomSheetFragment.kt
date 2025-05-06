package com.example.mobile.ui.ticket

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.mobile.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFragment(
    private val title:String,
    private val hint:String,
    private val firstActionButtonString:String,
    private val secondActionButtonString:String? = null,
    private val onFirstAction: (String) -> Unit,
    private val onSecondAction: ((String) -> Unit)? = null
) : BottomSheetDialogFragment() {
    private lateinit var btnCancel: Button
    private lateinit var tvTitle: TextView
    private lateinit var tilDescription: TextInputLayout
    private lateinit var btnFirstActionButton: Button
    private lateinit var btnSecondActionButton: Button
    private lateinit var etDescription: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ticket_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitle = view.findViewById(R.id.tv_title)
        tilDescription = view.findViewById(R.id.til_description)
        etDescription = view.findViewById(R.id.et_description)
        btnFirstActionButton = view.findViewById(R.id.btn_first_action)
        btnSecondActionButton = view.findViewById(R.id.btn_second_action)
        btnCancel = view.findViewById(R.id.btn_cancel)
        tvTitle.text = title
        tilDescription.hint = hint
        btnFirstActionButton.text = firstActionButtonString
        if(secondActionButtonString != null) {
            btnSecondActionButton.visibility = View.VISIBLE
            btnSecondActionButton.text = secondActionButtonString
        }

        // Set up text watcher to enable/disable buttons based on input
            etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val hasText = !s.isNullOrEmpty()
                btnFirstActionButton.isEnabled = hasText
                btnFirstActionButton.isEnabled = hasText
            }
        })

        // Set up button click listeners
            btnFirstActionButton.setOnClickListener {
            onFirstAction(etDescription.text.toString())
            dismiss()
        }
        if(onSecondAction!=null){
            btnSecondActionButton.setOnClickListener {
                onSecondAction.let { it1 -> it1(etDescription.text.toString()) }
                dismiss()
            }
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "BottomSheetFragment"
    }
}