package com.payday.paydaybank.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.payday.paydaybank.BuildConfig
import com.payday.paydaybank.MainActivity
import com.payday.paydaybank.auth.crypto.BioAuthenticator
import com.payday.paydaybank.auth.crypto.BiometricCryptoManager
import com.payday.paydaybank.auth.crypto.CryptoManager
import com.payday.paydaybank.auth.crypto.StubCryptoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton


@InstallIn(ActivityComponent::class)
@Module
object AuthModule {

    @Provides
    fun provideCryptoManager(@ApplicationContext context: Context, sharedPreferences: SharedPreferences): CryptoManager {
        return if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && BiometricManager.from(context).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            BiometricCryptoManager("PayDay", sharedPreferences)
        } else {
            StubCryptoManager()
        }
    }

    @Provides
    @ActivityScoped
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("PayDayPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @ActivityScoped
    fun provideBioAuthenticator(activity: FragmentActivity): BioAuthenticator {
        return BioAuthenticator((activity as MainActivity).provideCurrentFragment())
    }
}