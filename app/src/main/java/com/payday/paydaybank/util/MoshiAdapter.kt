package com.payday.paydaybank.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.math.BigDecimal


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class StringToBigDecimal


class StringToBigDecimalAdapter {

    @FromJson
    @StringToBigDecimal
    fun fromJson(value: String): BigDecimal {
        return BigDecimal(value)
    }

    @ToJson
    fun toJson(@StringToBigDecimal value: BigDecimal): String {
        return value.toString()
    }
}