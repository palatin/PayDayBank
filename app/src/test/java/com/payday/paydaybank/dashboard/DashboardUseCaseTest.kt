package com.payday.paydaybank.dashboard

import com.payday.paydaybank.model.Account
import com.payday.paydaybank.model.Transaction
import com.payday.paydaybank.model.category.Category
import com.payday.paydaybank.model.category.DefaultCategoryFactory
import com.payday.paydaybank.repository.TransactionRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyBoolean
import org.mockito.runners.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class DashboardUseCaseTest {

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    private lateinit var dashboardUseCase: DashboardUseCase

    @Before
    fun before() {
        dashboardUseCase = DashboardUseCase(transactionRepository, DefaultCategoryFactory(), Account(1, "", "", "", "", "", "", ""))
    }

    @Test
    fun getDashboardData_NoCurrentAccount() = runBlocking {
        `when`(transactionRepository.fetchTransactions(ArgumentMatchers.anyBoolean())).thenReturn(listOf<Transaction>(
            Transaction(accountId = 5, date = "2019-08-15T04:45:43Z"), Transaction(accountId = 3, date = "2019-08-15T04:45:43Z")))
        val data = dashboardUseCase.getDashboardData(false)
        assertTrue(data.data.isEmpty())
        assertTrue(data.totalExpensive == BigDecimal.ZERO)
        assertTrue(data.totalIncome == BigDecimal.ZERO)
    }

    @Test
    fun getDashboardData_TestObjectCountsByAccount() = runBlocking {
        `when`(transactionRepository.fetchTransactions(ArgumentMatchers.anyBoolean())).thenReturn(listOf<Transaction>(
            Transaction(accountId = 1, date = "2019-08-15T04:45:43Z"),
            Transaction(accountId = 3, date = "2019-08-15T04:45:43Z"),
            Transaction(accountId = 1, date = "2019-08-15T04:45:43Z")))
        val data = dashboardUseCase.getDashboardData(false)
        assertTrue(data.data.entries.first().value.size == 2)
    }

    @Test
    fun getDashboardData_TestCategories() = runBlocking {
        `when`(transactionRepository.fetchTransactions(ArgumentMatchers.anyBoolean())).thenReturn(listOf<Transaction>(
            Transaction(accountId = 1, category = "First", date = "2019-08-15T04:45:43Z"),
            Transaction(accountId = 1, category = "Second", date = "2019-08-15T04:45:43Z"),
            Transaction(accountId = 1, category = "First", date = "2019-08-15T04:45:43Z")))
        val data = dashboardUseCase.getDashboardData(false)
        assertTrue(data.data.containsKey(Category("First")) && data.data.containsKey(Category("Second")))
        assertTrue(data.data.getValue(Category("First")).size == 2)
    }

}