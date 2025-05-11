package com.example.mobile.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mobile.MainActivity
import com.example.mobile.MainActivity.Companion
import com.example.mobile.R
import com.example.mobile.ui.accountant.TicketListFragment
import com.example.mobile.ui.ticket.TicketViewModel
import com.example.mobile.utils.DialogType
import com.example.mobile.utils.UiState
import com.token.uicomponents.components330.dialog_box_fullscreen.DialogBoxFullScreen330
import com.token.uicomponents.components330.dialog_box_info.DialogBoxInfo330
import com.token.uicomponents.infodialog.InfoDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseFragment : Fragment() {

    protected val TAG: String = this::class.java.simpleName
    private var loadingDialog: InfoDialog? = null

    protected fun showLoading(message: String = "Loading...") {
        //TODO ASSURE SHOWLOADING FINISHES BEFORE HIDELOADING
        //lifecycleScope.launch() {
            hideLoading()  // önce eski loading varsa gizle
            loadingDialog = getDialog(DialogType.LOADING, message)
            //val showJob = async {
                loadingDialog?.show(parentFragmentManager, "LoadingDialog")
              //  parentFragmentManager.executePendingTransactions()
            //}
            //showJob.await()
            Log.i("BaseFragment", "Loading dialog is shown")
      //  }
    }
//TODO, CHECK OTHER OPTIONS FOR MAKING SHOW LOADING DIALOG ATOMIC
    protected fun <T> observeUiState(
        stateFlow: StateFlow<UiState<T>>,
        onSuccess: (T) -> Unit?,
        onError: (String) -> Unit? = { showError(it) },
        onLoading: () -> Unit? = { showLoading() },
        onIdle: () -> Unit? = { hideLoading() }
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            stateFlow.collect { state ->
                withContext(Dispatchers.Default) { //TODO REVERTED FROM MAIN ??
                    when (state) {
                        is UiState.Success -> {
                            Handler(Looper.getMainLooper()).postDelayed({
                                hideLoading()
                                onSuccess(state.data)

                            }, 200) //DONT USE HANDLER

                        }
                        is UiState.Error -> {
                            //handler delay for 0.5 seconds
                            Handler(Looper.getMainLooper()).postDelayed({
                                hideLoading()
                                onError(state.message)

                            }, 200) //DONT USE HANDLER
                            //hideLoading()
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
    }


    protected fun hideLoading() {
    //    lifecycleScope.launch {
//            val hideJob = async {
//                loadingDialog?.dismiss()
//                loadingDialog = null
//            }
//            hideJob.await()  // Dismiss işlemi tamamlanana kadar bekle
            loadingDialog?.dismiss()
           loadingDialog = null
            Log.i("BaseFragment", "Loading dialog dismissed")
      //  }
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
    protected fun getTicketList(viewModel: TicketViewModel, myFunc: ()-> Unit) {
            Log.i(TAG, "List Closed Tickets button clicked")
            myFunc()
            observeUiState(
                viewModel.getTicketListIdState,
                onSuccess = { data ->
                    Log.i(TAG, "Success: $data")
                    val ticketList = data
                    Log.i(TAG, "Ticket List: $ticketList")
                    System.out.print("hi")
                    val ticketListFragment = TicketListFragment(ticketList)
                    replaceFragment(ticketListFragment)
                },
                onError = {
                    Log.e(TAG, "Error fetching ticket ids: $it")
                    getDialog(DialogType.ERROR,it).show(requireActivity().supportFragmentManager, "ErrorDialog")
                    popFragment()
                },
                onLoading = {
                    showLoading()
                },
                onIdle = {
                    hideLoading()
                }
            )

    }
}

