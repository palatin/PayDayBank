package com.payday.paydaybank.auth.crypto

import android.util.Base64
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.SecretKey

interface CryptoManager {

    fun decrypt(data: String, cipher: Cipher): String {
        return String(cipher.doFinal(Base64.decode(data, Base64.DEFAULT)))
    }

    fun encrypt(data: String, cipher: Cipher): String {
        return Base64.encodeToString(cipher.doFinal(data.toByteArray()), Base64.DEFAULT)
    }


    fun getEncryptionCipher(): Cipher

    fun getDecryptionCipher(): Cipher

    fun shouldAuthBio(): Boolean = false

}