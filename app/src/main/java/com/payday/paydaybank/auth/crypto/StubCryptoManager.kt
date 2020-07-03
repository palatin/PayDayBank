package com.payday.paydaybank.auth.crypto

import javax.crypto.Cipher

class StubCryptoManager : CryptoManager {

    private val stubCipher = Cipher.getInstance("AES")

    override fun getEncryptionCipher(): Cipher {
        return stubCipher
    }

    override fun getDecryptionCipher(): Cipher {
        return stubCipher
    }

    override fun encrypt(data: String, cipher: Cipher): String {
        return data
    }

    override fun decrypt(data: String, cipher: Cipher): String {
        return data
    }
}