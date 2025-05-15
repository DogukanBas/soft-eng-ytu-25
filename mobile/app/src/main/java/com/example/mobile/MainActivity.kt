package com.example.mobile

import GenerateReportFragment
import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile.databinding.ActivityMainBinding
import com.example.mobile.ui.dialog.IpDialogFragment
import com.example.mobile.ui.login.LoginFragment
import com.example.mobile.utils.PrefsUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefsUtil: PrefsUtil

    companion object {
        const val TAG = "MainActivity"
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.i(TAG, "Camera permission result: $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()
        
        prefsUtil = PrefsUtil(this)
        showIpDialog()
    }

    private fun showIpDialog() {
       // val dialog = IpDialogFragment()
       // dialog.setOnIpSelectedListener { ip ->
         //   prefsUtil.saveIpAddress(ip)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, LoginFragment())
                commit()
            }
       // }
        //dialog.show(supportFragmentManager, "IpDialog")
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

    fun requestPermissions() {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}