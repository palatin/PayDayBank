package com.payday.paydaybank.repository

import com.payday.paydaybank.api.TransactionApi
import com.payday.paydaybank.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val transitionApi: TransactionApi) {


    suspend fun fetchTransactions(forceUpdate: Boolean = false): List<Transaction> {

        return withContext(Dispatchers.IO) {
            if(!forceUpdate) {
                try {
                    getTransactionsFromDb()
                } catch (ex: Exception) {
                    getTransactionsFromNetwork()
                }
            }
            else {
                getTransactionsFromNetwork()
            }
        }
    }

    private suspend fun getTransactionsFromDb(): List<Transaction> = throw Exception()

    private suspend fun getTransactionsFromNetwork(): List<Transaction> = transitionApi.getTransactions()
}