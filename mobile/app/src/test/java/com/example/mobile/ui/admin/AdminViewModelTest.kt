package com.example.mobile.ui.admin

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobile.model.User.Employee
import com.example.mobile.model.User.UserType
import com.example.mobile.remote.dtos.Admin.AddUserResponse
import com.example.mobile.repositories.AdminRepository
import com.example.mobile.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import io.mockk.*

@ExperimentalCoroutinesApi
class AdminViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AdminViewModel

    @Mock
    private lateinit var adminRepository: AdminRepository


    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        viewModel = AdminViewModel(adminRepository)
    }

    @Test
    fun `addUser success should update addUserState to Success`() = runTest {
        val employee = Employee(UserType.TEAM_MEMBER, "User", "123", "test@example.com", "pass", "01", "IT")
        val successResponse = AddUserResponse("Employee added successfully")
        `when`(adminRepository.addUser(employee)).thenReturn(Result.success(successResponse))

        viewModel.addUser(employee)

        val addUserState = viewModel.addUserState.value
        assert(addUserState is UiState.Success)
        assertEquals(successResponse, (addUserState as UiState.Success).data)
    }

    @Test
    fun `addUser failure should update addUserState to Error`() = runTest {
        val employee = Employee(UserType.TEAM_MEMBER, "User", "123", "test@example.com", "pass", "123", "IT")
        val errorMessage = "Employee already exists"
        `when`(adminRepository.addUser(employee)).thenReturn(Result.failure(Exception(errorMessage)))

        viewModel.addUser(employee)

        val addUserState = viewModel.addUserState.value
        assert(addUserState is UiState.Error)
        assertEquals(errorMessage, (addUserState as UiState.Error).message)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}