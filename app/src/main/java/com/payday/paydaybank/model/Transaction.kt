package com.payday.paydaybank.model

import com.payday.paydaybank.util.DateTimeFormat
import com.payday.paydaybank.util.StringToBigDecimal
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class Transaction(
    val id: Int = 0,
    @Json(name = "account_id")
    val accountId: Int = 0,
    @StringToBigDecimal
    val amount: BigDecimal = BigDecimal.ZERO,
    val vendor: String = "",
    val category: String = "",
    val date: String = "",
    @Transient
    val dateTimeStamp: Long = DateTimeFormat.parse(date)!!.time
)
