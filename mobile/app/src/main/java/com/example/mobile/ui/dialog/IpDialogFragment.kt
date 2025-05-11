package com.example.mobile.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.mobile.remote.RetrofitClient

class IpDialogFragment : DialogFragment() {
    private var onIpSelected: ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val input = EditText(requireContext())
        input.hint = "Backend IP Address (e.g.: 192.168.1.100)"
        
        return AlertDialog.Builder(requireContext())
            .setTitle("Backend IP Address")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                val ip = input.text.toString()
                if (ip.isNotEmpty()) {
                    onIpSelected?.invoke(ip)
                    RetrofitClient.updateBaseUrl(ip)
                }
            }
            .create()
    }

    fun setOnIpSelectedListener(listener: (String) -> Unit) {
        onIpSelected = listener
    }
}