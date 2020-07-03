package com.payday.paydaybank.api

import com.payday.paydaybank.model.Account
import com.payday.paydaybank.model.SignInData
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/authenticate")
    suspend fun signIn(@Body signInData: SignInData): Account

}