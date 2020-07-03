package com.payday.paydaybank.di.modules

import android.app.Activity
import com.payday.paydaybank.MainActivity
import com.payday.paydaybank.model.Account
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    @Provides
    @ActivityScoped
    fun provideAccount(activity: Activity): Account {
        return (activity as MainActivity).account!!
    }


}