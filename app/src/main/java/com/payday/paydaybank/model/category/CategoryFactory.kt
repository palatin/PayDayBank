package com.payday.paydaybank.model.category

import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped

@ActivityScoped
interface CategoryFactory {

    fun fromName(name: String): Category
}