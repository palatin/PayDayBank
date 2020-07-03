package com.payday.paydaybank.model.category

import androidx.annotation.ColorRes
import java.math.BigDecimal

data class Category(val name: String,
                    val icon: Int = 0,
                    @ColorRes
                    val color: Int = 0,
                    var amount: BigDecimal = BigDecimal(0.0)
) {

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Category && other.name == name
    }
}