package com.payday.paydaybank.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignInData(val email: String, val password: String)