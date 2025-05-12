package com.example.mobile.ui.ticket

import com.example.mobile.model.Ticket.Ticket
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.ui.BaseFragment
import com.example.mobile.utils.DialogType
import com.token.uicomponents.CustomInput.CustomInputFormat
import com.token.uicomponents.CustomInput.EditTextInputType
import com.token.uicomponents.CustomInput.InputListFragment
import com.token.uicomponents.CustomInput.InputValidator
import com.token.uicomponents.components330.dialog_box_info.DialogBoxInfo330
import com.token.uicomponents.components330.input_menu_fragment.InputMenuFragment330
import com.token.uicomponents.infodialog.InfoDialog
import com.token.uicomponents.infodialog.InfoDialogListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class CreateTicketFragment(private val costTypes: List<String>) : BaseFragment() {

    companion object {
        val TAG = "CreateTicketFragment"
        private const val MAX_IMAGE_SIZE = 1024 * 1024 * 10 // 10MB
    }

    private var pendingTicket: Ticket? = null
    private val viewModel: TicketViewModel by viewModels()

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                val base64Image = convertBitmapToBase64(it)
                updateTicketWithInvoice(base64Image)
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            val base64Image = convertUriToBase64(it)
            updateTicketWithInvoice(base64Image)
        }
    }

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
            .replace(R.id.input_menu_container, menuFragment)
            .commit()
    }

    private fun setStateCollectors() {
        Log.i(TAG, "Setting up state collectors")
        val dialog = getDialog(DialogType.LOADING, "Loading")

        observeUiState(
            viewModel.createTicketState,
            onSuccess = { data ->
                Log.i(TAG, "Success: $data")
                dialog.dismiss()
                getDialog(DialogType.SUCCESS, data.message + " \n Afforded amount is  " + data.budget.toString()).show(
                    requireActivity().supportFragmentManager, "SuccessDialog"
                )
                popFragment()
            },
            onError = { message ->
                Log.e(TAG, "Error: $message")
                dialog.dismiss()
                popFragment()
                getDialog(DialogType.ERROR, message).show(requireActivity().supportFragmentManager, "ErrorDialog")
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

    private fun showImageSelectionDialog() {
        val dialog = DialogBoxInfo330(
            "Select Image Source",
            info = "Choose how to add invoice image",
            isCancelable = true,
            infoDialogButtons = InfoDialog.InfoDialogButtons.Both,
            nameBtnOK = "Camera",
            nameBtnCancel = "Gallery",
            infoDialogListener = object : InfoDialogListener {
                override fun confirmed(arg: Int) {
                    Log.i(TAG, "Camera selected")
                    openCamera()
                }
                override fun canceled(arg: Int) {
                    Log.i(TAG, "Gallery selected")
                    openGallery()
                }
            }
        )
        dialog.show(requireActivity().supportFragmentManager, "ImageSourceDialog")
    }

    private fun openCamera() {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening camera: ${e.message}")
            (activity as? MainActivity)?.requestPermissions()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun convertUriToBase64(uri: Uri): String {
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun updateTicketWithInvoice(base64Image: String) {
        pendingTicket?.let { ticket ->
            Log.i(TAG, "Creating ticket with invoice")
            val cleanBase64Image = base64Image.replace("\n", "").replace("\r", "")
            Log.i(TAG, "Base64 Image: $cleanBase64Image")
            ticket.invoice = cleanBase64Image
            viewModel.createTicket(ticket)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setMenu(): InputListFragment {
        val inputList: MutableList<CustomInputFormat> = mutableListOf()
        inputList.add(CustomInputFormat("Select Cost Type", EditTextInputType.MenuList, null, null,
            { input -> input.text.isNotEmpty() }).apply {
            singleSelectionListItems = costTypes
            text = singleSelectionListItems[0]
        })
        inputList.add(
            CustomInputFormat(
                "Enter Amount",
                EditTextInputType.Number,
                100,
                true,
                "Boş Bırakılamaz",
                { input ->
                    input.text.isNotEmpty() && input.text.toDoubleOrNull() != null
                }
            )
        )
        inputList.add(
            CustomInputFormat(
                "Enter Date",
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
            }
        )

        return InputMenuFragment330(inputList, { outputList ->
            Log.i(TAG, "Create com.example.mobile.model.Ticket.Ticket button triggered")
            val costType = outputList[0].toString()
            val amount = outputList[1].toString().toDoubleOrNull()

            val array = outputList[2].toString().split("/").toTypedArray()
            val date = array[2].substring(2) + array[1] + array[0]
            val description = outputList[3].toString()

            val formatter = DateTimeFormatter.ofPattern("yyMMdd")
            val updatedDate = LocalDate.parse(date, formatter)

            Log.i(TAG, "All data: $costType, $amount, $date and updated date = ${updatedDate::class.simpleName}, $description")
            Log.i(TAG, "Creating ticket without invoice")

            pendingTicket = Ticket(
                costType = costType,
                amount = amount ?: 0.0,
                description = description,
                date = updatedDate.toString(),
                invoice = ""
            )

            showImageSelectionDialog()
        }, "Create com.example.mobile.model.Ticket.Ticket")
    }
}


