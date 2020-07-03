package com.payday.paydaybank.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

inline fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun NavController.navigateSafe(@IdRes resId: Int,
                               args: Bundle? = null,
                               navOptions: NavOptions? = null,
                               navExtras: Navigator.Extras? = null) {

    val action = currentDestination?.getAction(resId)
    if (action != null) navigate(resId, args, navOptions, navExtras)
}