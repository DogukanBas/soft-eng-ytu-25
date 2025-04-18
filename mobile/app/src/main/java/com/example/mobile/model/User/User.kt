package com.example.mobile.model.User

object User {
    var personalNo: String = ""
    var email: String = ""
    var userType: UserType? =null
    var accessToken: String? = null
    var name: String? = null
    var surname: String? = null
    fun clear() {
        personalNo = ""
        email = ""
        userType = null
        accessToken = null
        name = null
        surname = null
    }
    fun isLoggedIn(): Boolean {
        return !(accessToken.isNullOrBlank())
    }
    fun isAdmin(): Boolean {
        return userType == UserType.ADMIN
    }
    fun isManager(): Boolean {
        return userType == UserType.MANAGER
    }
    fun isAccountant(): Boolean {
        return userType == UserType.ACCOUNTANT
    }
    fun isTeamMember(): Boolean {
        return userType == UserType.TEAM_MEMBER
    }
    fun setUser(
        personalNo: String,
        email: String,
        userType: UserType,
        accessToken: String,

    ) {
        this.personalNo = personalNo
        this.email = email
        this.userType = userType
        this.accessToken = accessToken

    }
    fun setNameAndSurname(
        name: String,
        surname: String
    ) {
        this.name = name
        this.surname = surname
    }

}
