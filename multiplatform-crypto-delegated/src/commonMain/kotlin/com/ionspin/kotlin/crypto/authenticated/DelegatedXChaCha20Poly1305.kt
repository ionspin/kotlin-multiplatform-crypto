package com.ionspin.kotlin.crypto.authenticated


/**
 * Created by Ugljesa Jovanovic
 * ugljesa.jovanovic@ionspin.com
 * on 14-Jun-2020
 */
expect class XChaCha20Poly1305Delegated internal constructor() {
    internal constructor(key: UByteArray, testState : UByteArray, testHeader: UByteArray, isDecryptor: Boolean)
    companion object {
        fun encrypt(key: UByteArray, nonce: UByteArray, message: UByteArray, associatedData: UByteArray) : UByteArray
        fun decrypt(key: UByteArray, nonce: UByteArray, ciphertext: UByteArray, associatedData: UByteArray) : UByteArray
    }
    fun initializeForEncryption(key: UByteArray) : UByteArray
    fun initializeForDecryption(key: UByteArray, header: UByteArray)
    fun encrypt(data: UByteArray, associatedData: UByteArray = ubyteArrayOf()) : UByteArray
    fun decrypt(data: UByteArray, associatedData: UByteArray = ubyteArrayOf()) : UByteArray
    fun cleanup()


}


