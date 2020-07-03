package com.payday.paydaybank.dashboard

import com.payday.paydaybank.model.Account
import com.payday.paydaybank.model.category.Category
import com.payday.paydaybank.model.Transaction
import com.payday.paydaybank.model.category.CategoryFactory
import com.payday.paydaybank.repository.TransactionRepository
import com.payday.paydaybank.util.sumByFloat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.math.abs

class DashboardUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryFactory: CategoryFactory,
    private val account: Account
) {

    suspend fun getDashboardData(forceUpdate: Boolean = false): DashboardData {
        return getDashboardData(forceUpdate) { it }
    }

    suspend fun getDashboardData(forceUpdate: Boolean = false, timeStampRange: LongRange): DashboardData {
        //TODO sort
        return getDashboardData(forceUpdate) { it.filter { it.dateTimeStamp in timeStampRange } }
    }

    //Inlining this function causes internal kotlin compiler bug
    private suspend fun getDashboardData(forceUpdate: Boolean = false, transform: (List<Transaction>) -> List<Transaction>): DashboardData {
        return withContext(Dispatchers.Default) {
            val data = groupDataByCategories(transform.invoke(getTransactionsByAccount(forceUpdate, account.id)))
            val amounts = getTotalIncomeAndExpensive(data.keys)
            DashboardData(data, amounts.first, amounts.second)
        }
    }

    private suspend fun getTransactionsByAccount(forceUpdate: Boolean = false, accountId: Int): List<Transaction> {
        return transactionRepository.fetchTransactions(forceUpdate)
            .filter { it.accountId == accountId }.sortedByDescending { it.dateTimeStamp }
    }

    private fun groupDataByCategories(transactions: List<Transaction>): Map<Category, List<Transaction>> {
        return transactions.groupBy { categoryFactory.fromName(it.category) }.also {
            it.forEach { entry ->
                entry.value.forEach { entry.key.amount += it.amount }
            }
        }
    }

    private fun getTotalIncomeAndExpensive(categories: Set<Category>): Pair<BigDecimal, BigDecimal> {
        var income = BigDecimal.ZERO
        var expensive = BigDecimal.ZERO
        categories.forEach {
            if(it.amount > BigDecimal.ZERO)
                income += it.amount
            else
                expensive -= it.amount
        }
        return income to expensive
    }


    data class DashboardData(val data: Map<Category, List<Transaction>>, val totalIncome: BigDecimal, val totalExpensive: BigDecimal)

}