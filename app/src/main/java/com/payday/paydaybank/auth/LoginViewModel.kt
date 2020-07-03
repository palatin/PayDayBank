package com.payday.paydaybank.auth


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.core.content.edit
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payday.paydaybank.auth.crypto.BioAuthenticator
import com.payday.paydaybank.auth.crypto.CryptoManager
import com.payday.paydaybank.model.Account
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import javax.inject.Inject

class LoginViewModel @ViewModelInject constructor(private val cryptoManager: CryptoManager,
                                                  private val sharedPreferences: SharedPreferences,
                                                  private val bioAuthenticator: BioAuthenticator,
                                                  private val accountRepository: AccountRepository) : ViewModel() {

    private val actionChannel = BroadcastChannel<Action>(1)
    val actionFlow = actionChannel.asFlow()

    fun processIntent(intent: Intent) {
        when(intent) {
            is Intent.SignInClicked -> {
                viewModelScope.launch {
                    if(validateData(intent.email, intent.password)) {
                        signIn(intent.email, intent.password, true)
                    }
                }
            }
            Intent.TrySignIn -> viewModelScope.launch {
                try {
                    signIn(sharedPreferences.getString("email", null)!!, getPassword())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
        }
    }

    private fun signIn(email: String, password: String, updatePassword: Boolean = false) {
        viewModelScope.launch {
            try {
                val account = accountRepository.signIn(email, password)
                try {
                    if(updatePassword) {
                        storePassword(password)
                        sharedPreferences.edit(true) {
                            putString("email", email)
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

                actionChannel.send(Action.OpenDashboard(account))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private suspend fun getPassword(): String {
        if(!sharedPreferences.contains("password")) {
            throw Exception()
        }
        val password = sharedPreferences.getString("password", "")!!
        val cipher = if(cryptoManager.shouldAuthBio()) bioAuthenticator.auth(cryptoManager.getDecryptionCipher()).await() else cryptoManager.getEncryptionCipher()
        return cryptoManager.decrypt(password, cipher)

    }

    private suspend fun storePassword(password: String) {
        val cipher = if(cryptoManager.shouldAuthBio()) bioAuthenticator.auth(cryptoManager.getEncryptionCipher()).await() else cryptoManager.getEncryptionCipher()

        val password = cryptoManager.encrypt(password, cipher)
        sharedPreferences.edit(true) {
            putString("password", password)
        }
    }

    private suspend fun validateData(email: String, password: String): Boolean {
        var isValid = true
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            actionChannel.send(Action.ShowInvalidEmailValidation)
            isValid = false
        }
        if(password.length < 6 || !password.matches(Regex("[A-Za-z0-9]+"))) {
            actionChannel.send(Action.ShowInvalidPasswordValidation)
            isValid = false
        }

        return isValid
    }


    sealed class Intent {
        data class SignInClicked(val email: String, val password: String) : Intent()
        object TrySignIn : Intent()
    }

    sealed class Action {
        data class OpenDashboard(val account: Account) : Action()
        object ShowInvalidEmailValidation : Action()
        object ShowInvalidPasswordValidation : Action()
    }
}