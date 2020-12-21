package com.payday.paydaybank.auth.crypto

import android.app.Activity
import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.inject.Inject

@ActivityScoped
class BioAuthenticator @Inject constructor(private val fragment: Fragment) {

    fun auth(cipher: Cipher): Deferred<Cipher> {
        val completableDeferred = CompletableDeferred<Cipher>()
        val biometricPrompt = BiometricPrompt(fragment, ContextCompat.getMainExecutor(fragment.requireContext()), object :
            BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                if(result.cryptoObject?.cipher != null) {
                    completableDeferred.complete(result.cryptoObject!!.cipher!!)
                } else {
                    completableDeferred.completeExceptionally(Exception())
                }
            }

            override fun onAuthenticationFailed() {
                completableDeferred.completeExceptionally(Exception())
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                completableDeferred.completeExceptionally(Exception(errString.toString()))
            }
        })

        biometricPrompt.authenticate(BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build(), BiometricPrompt.CryptoObject(cipher)
        )
        return completableDeferred
    }
}
