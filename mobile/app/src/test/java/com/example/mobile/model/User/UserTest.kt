package com.example.mobile.model.User

import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserTest {

    @Before
    fun setUp() {
        // Clear user state before each test
        User.clear()
    }

    @Test
    fun `isLoggedIn should return true when accessToken is not blank`() {
        User.accessToken = "sample_token"
        assertTrue(User.isLoggedIn())
    }

    @Test
    fun `isLoggedIn should return false when accessToken is null`() {
        User.accessToken = null
        assertFalse(User.isLoggedIn())
    }

    @Test
    fun `isLoggedIn should return false when accessToken is blank`() {
        User.accessToken = " "
        assertFalse(User.isLoggedIn())
    }

    @Test
    fun `isAdmin should return true when userType is ADMIN`() {
        User.userType = UserType.ADMIN
        assertTrue(User.isAdmin())
    }

    @Test
    fun `isAdmin should return false when userType is not ADMIN`() {
        User.userType = UserType.MANAGER
        assertFalse(User.isAdmin())

        User.userType = UserType.ACCOUNTANT
        assertFalse(User.isAdmin())

        User.userType = UserType.TEAM_MEMBER
        assertFalse(User.isAdmin())
    }

    @Test
    fun `isAdmin should return false when userType is null`() {
        User.userType = null
        assertFalse(User.isAdmin())
    }

    @After
    fun tearDown() {
        // Clear user state after each test
        User.clear()
    }
}