package com.payday.paydaybank.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.payday.paydaybank.model.category.CategoryFactory
import com.payday.paydaybank.model.category.DefaultCategoryFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class DashboardModule {


    @Binds
    abstract fun bindCategoryFactory(categoryFactory: DefaultCategoryFactory): CategoryFactory

}