package com.example.mobile.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mobile.MainActivity
import com.example.mobile.MainActivity.Companion
import com.example.mobile.R
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.UiState
import com.token.uicomponents.components330.dialog_box_fullscreen.DialogBoxFullScreen330
import com.token.uicomponents.components330.dialog_box_info.DialogBoxInfo330
import com.token.uicomponents.infodialog.InfoDialog
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseFragment : Fragment() {

    protected val TAG: String = this::class.java.simpleName
    private var loadingDialog: InfoDialog? = null

    protected fun showLoading(message: String = "Loading...") {
        hideLoading()
        loadingDialog = getDialog(DialogType.LOADING, message)
        loadingDialog?.show(parentFragmentManager, "LoadingDialog")
    }
    protected fun <T> observeUiState(
        stateFlow: StateFlow<UiState<T>>,
        onSuccess: (T) -> Unit?,
        onError: (String) -> Unit? = { showError(it) },
        onLoading: () -> Unit? = { showLoading() },
        onIdle: () -> Unit? = { hideLoading() }
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
                stateFlow.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            hideLoading()
                            onSuccess(state.data)
                        }
                        is UiState.Error -> {
                            hideLoading()
                            onError(state.message)
                        }
                        is UiState.Loading -> {
                            onLoading()
                        }
                        is UiState.Idle -> {
                            onIdle()
                        }
                    }
                }
            }
        }


    protected fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    protected fun showError(message: String) {
        val dialog = getDialog(DialogType.ERROR, message)
        dialog.show(parentFragmentManager, "ErrorDialog")
    }

    protected fun showSuccess(message: String) {
        val dialog = getDialog(DialogType.SUCCESS, message)
        dialog.show(parentFragmentManager, "SuccessDialog")
    }

    protected fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    protected fun popFragment() {
        Log.i(Companion.TAG, "Popping fragment")
        val fragment = (activity as MainActivity).supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null) {
            (activity as MainActivity).supportFragmentManager.popBackStack()
        }
    }

    fun getDialog(dialogType: DialogType, message:String ): InfoDialog {
        Log.i(MainActivity.TAG, "getDialog called with type: $dialogType")
        val dialog = when (dialogType) {
            DialogType.LOADING -> DialogBoxFullScreen330(
                InfoDialog.InfoType.Processing,
                message,
                isCancelable = false)
            DialogType.ERROR -> DialogBoxInfo330(
                "Error",
                isCancelable= true,
                info = message,
                infoDialogButtons = InfoDialog.InfoDialogButtons.Confirm

            )
            DialogType.SUCCESS ->
                DialogBoxInfo330(
                    "Success",
                    info = message,
                    isCancelable = true,
                    infoDialogButtons = InfoDialog.InfoDialogButtons.Confirm
                )
        }
        return dialog
    }
}
