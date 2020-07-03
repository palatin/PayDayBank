package com.payday.paydaybank.api

import com.payday.paydaybank.model.Transaction
import retrofit2.http.GET

interface TransactionApi {

    @GET("/transactions")
    suspend fun getTransactions(): List<Transaction>
}