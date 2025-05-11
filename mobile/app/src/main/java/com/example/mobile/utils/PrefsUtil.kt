package com.example.mobile.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PrefsUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("NetworkPrefs", Context.MODE_PRIVATE)

    fun saveIpAddress(ip: String) {
        prefs.edit() { putString("IP_ADDRESS", ip) }
    }

    fun getIpAddress(): String? {
        return prefs.getString("IP_ADDRESS", null)
    }
}