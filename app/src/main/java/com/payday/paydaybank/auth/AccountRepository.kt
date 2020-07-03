package com.payday.paydaybank.auth

import com.payday.paydaybank.api.AuthApi
import com.payday.paydaybank.model.Account
import com.payday.paydaybank.model.SignInData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(private val authApi: AuthApi) {



    suspend fun signIn(email: String, password: String): Account {
        return Account(1, "Test", "Test", "test", email, password, "", "")
        //authenticate API not working
        //return withContext(Dispatchers.IO) { authApi.signIn(SignInData(email, password)) }
    }

}