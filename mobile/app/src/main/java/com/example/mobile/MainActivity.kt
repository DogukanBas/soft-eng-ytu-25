package com.example.mobile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mobile.databinding.ActivityMainBinding
import com.example.mobile.ui.login.LoginFragment
import com.example.mobile.utils.DialogType
import com.token.uicomponents.components330.dialog_box_fullscreen.DialogBoxFullScreen330
import com.token.uicomponents.components330.dialog_box_info.DialogBoxInfo330
import com.token.uicomponents.infodialog.InfoDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()
        replaceFragment(LoginFragment(),false)
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }


    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun popFragment() {
        Log.i(TAG, "Popping fragment")
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null) {
            supportFragmentManager.popBackStack()
        }
    }
    fun getDialog(dialogType: DialogType, message:String ): InfoDialog {
        Log.i(TAG, "getDialog called with type: $dialogType")
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