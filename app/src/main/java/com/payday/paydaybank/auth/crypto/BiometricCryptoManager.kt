package com.payday.paydaybank.auth.crypto


import android.content.SharedPreferences
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@RequiresApi(Build.VERSION_CODES.M)
class BiometricCryptoManager constructor(private val keyName: String, private val sharedPreferences: SharedPreferences) : CryptoManager {


    private val keyGenParameterSpec = KeyGenParameterSpec.Builder(
        keyName,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        .setUserAuthenticationRequired(true)
        .build()


    private var secretKey: SecretKey? = null

    private fun getSecretKey(): SecretKey {


        if(secretKey != null)
            return secretKey!!
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        keyStore.load(null)
        return (keyStore.getKey(keyName, null) as? SecretKey ?:
                kotlin.run {
                    val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
                    keyGenerator.init(keyGenParameterSpec)
                    keyGenerator.generateKey()
                }).also { this.secretKey = it }

    }

    override fun getDecryptionCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7).apply {
            val IV = sharedPreferences.getString("IV", null)!!
            init(Cipher.DECRYPT_MODE, getSecretKey(), IvParameterSpec(Base64.decode(IV.toByteArray(
                Charset.forName("UTF-8")), Base64.DEFAULT)))
        }
    }

    override fun getEncryptionCipher(): Cipher {
        return Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7
        ).apply {
            init(Cipher.ENCRYPT_MODE, getSecretKey())
            sharedPreferences.edit(true) {
                putString("IV", Base64.encodeToString(iv, Base64.DEFAULT))
            }
        }
    }


    override fun shouldAuthBio(): Boolean {
        return true
    }

}