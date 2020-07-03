package com.payday.paydaybank.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object CategoryComponent {

    @Provides
    @FragmentScoped
    fun provideLifecycleCoroutine(fragment: Fragment) = fragment.lifecycleScope
}