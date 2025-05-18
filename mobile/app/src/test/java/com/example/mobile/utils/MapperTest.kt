package com.example.mobile.utils

import com.example.mobile.model.User.Employee
import com.example.mobile.model.User.UserType
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperTest {

    @Test
    fun `employee toDto should map correctly to AddUserRequest`() {
        val employee = Employee(
            name = "Berkan",
            surname = "Eti",
            personalNo = "PN123",
            email = "Berkan.Eti@example.com",
            password = "securePassword",
            userType = UserType.MANAGER,
            department = "Sales"
        )

        val addUserRequest = employee.toDto()

        assertEquals("Berkan", addUserRequest.name)
        assertEquals("Eti", addUserRequest.surname)
        assertEquals("PN123", addUserRequest.personalNo)
        assertEquals("Berkan.Eti@example.com", addUserRequest.email)
        assertEquals("securePassword", addUserRequest.password)
        assertEquals("manager", addUserRequest.userType)
        assertEquals("Sales", addUserRequest.deptName)
    }
}