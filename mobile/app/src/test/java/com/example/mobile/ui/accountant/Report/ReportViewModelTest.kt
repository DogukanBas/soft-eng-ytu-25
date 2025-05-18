package com.example.mobile.ui.accountant.Report

import com.example.mobile.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mockito.mock
import com.example.mobile.repositories.ReportRepository
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * ReportViewModel için unit testler
 */
@ExperimentalCoroutinesApi
class ReportViewModelTest {

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val mainCoroutineRule = object : TestRule {
        override fun apply(base: Statement, description: Description): Statement {
            return object : Statement() {
                override fun evaluate() {
                    Dispatchers.setMain(testDispatcher)
                    try {
                        base.evaluate()
                    } finally {
                        Dispatchers.resetMain()
                    }
                }
            }
        }
    }

    private lateinit var viewModel: ReportViewModel
    private lateinit var repository: ReportRepository

    @Before
    fun setUp() {
        repository = mock(ReportRepository::class.java)
        viewModel = ReportViewModel(repository)
    }

    @Test
    fun `resetEntitityListUiState should reset state to Idle`() {
        viewModel.resetEntitityListUiState()
        
        assertTrue("EntityListUiState should be Idle after reset", viewModel.getEntity.value is UiState.Idle)
    }

}