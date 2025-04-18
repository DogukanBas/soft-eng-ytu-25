package com.example.mobile.model.User

enum class UserType {

    ADMIN,
    TEAM_MEMBER,
    ACCOUNTANT,
    MANAGER;
    companion object {
        fun fromString(value: String): UserType {
            return when (value) {
                "admin" -> ADMIN
                "team_member" -> TEAM_MEMBER
                "accountant" -> ACCOUNTANT
                "manager" -> MANAGER
                else -> throw IllegalArgumentException("Unknown user type: $value")

            }
        }
    }

}