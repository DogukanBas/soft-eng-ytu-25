package com.example.mobile.ui.ticket

import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.mobile.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InvoiceDialogFragment(private val invoiceString : String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.TransparentDialog)
        dialog.setContentView(R.layout.dialog_invoice)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val invoiceIv = dialog.findViewById<ImageView>(R.id.invoiceImage)
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val bitmap = withContext(Dispatchers.IO) {
                    val base64Data = if (invoiceString.contains(",")) {
                        invoiceString.substringAfter(",")
                    } else {
                        invoiceString
                    }

                    Log.i("InvoiceDialogFragment", "Base64 Data: $base64Data")
                    val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                }

                // Set bitmap on main thread
                invoiceIv.setImageBitmap(bitmap)

            } catch (e: Exception) {
                Log.e("InvoiceDialogFragment", "Error loading invoice", e)
            }
        }

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }
}