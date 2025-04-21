package com.example.mobile.model.User

import android.util.Log

enum class UserType {

    ADMIN,
    TEAM_MEMBER,
    ACCOUNTANT,
    MANAGER;
    companion object {
        fun fromString(value: String): UserType {
            return when (value) {
                "Admin" -> ADMIN
                "admin" -> ADMIN
                "team_member" -> TEAM_MEMBER
                "Team Member" -> TEAM_MEMBER
                "Accountant" -> ACCOUNTANT
                "accountant" -> ACCOUNTANT
                "Manager" -> MANAGER
                "manager" -> MANAGER
                else -> throw IllegalArgumentException("Unknown user type: $value")

            }
        }


    }
    override fun toString(): String {
        Log.i("UserType", "toString() called")
        return super.toString().lowercase()
    }

}