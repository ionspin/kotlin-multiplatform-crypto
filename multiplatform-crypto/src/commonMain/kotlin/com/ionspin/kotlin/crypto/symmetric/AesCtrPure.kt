/*
 *    Copyright 2019 Ugljesa Jovanovic
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.ionspin.kotlin.crypto.symmetric

import com.ionspin.kotlin.bignum.Endianness
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import com.ionspin.kotlin.bignum.modular.ModularBigInteger
import com.ionspin.kotlin.crypto.SRNG
import com.ionspin.kotlin.crypto.symmetric.AesCtrPure.Companion.encrypt
import com.ionspin.kotlin.crypto.util.xor

/**
 *
 *  Advanced encryption standard with counter mode
 *
 * For bulk encryption/decryption use [AesCtrPure.encrypt] and [AesCtrPure.decrypt]
 *
 * To get an instance of AesCtr and then feed it data sequentially with [addData] use [createEncryptor] and [createDecryptor]
 *
 * Created by Ugljesa Jovanovic
 * ugljesa.jovanovic@ionspin.com
 * on 22-Sep-2019
 */

internal class AesCtrPure internal constructor(val aesKey: InternalAesKey, val mode: Mode, initialCounter: UByteArray? = null) {

    companion object {
        const val BLOCK_BYTES = 16

        val modularCreator = ModularBigInteger.creatorForModulo(BigInteger.ONE.shl(128) - 1)
        /**
         * Creates and returns AesCtr instance that can be fed data using [addData]. Once you have submitted all
         * data call [encrypt]
         */
        fun createEncryptor(aesKey: InternalAesKey) : AesCtrPure {
            return AesCtrPure(aesKey, Mode.ENCRYPT)
        }
        /**
         * Creates and returns AesCtr instance that can be fed data using [addData]. Once you have submitted all
         * data call [decrypt]
         */
        fun createDecryptor(aesKey : InternalAesKey, initialCounter: UByteArray) : AesCtrPure {
            return AesCtrPure(aesKey, Mode.DECRYPT, initialCounter)
        }
        /**
         * Bulk encryption, returns encrypted data and a random initial counter
         */
        fun encrypt(aesKey: InternalAesKey, data: UByteArray): EncryptedDataAndInitialCounter {
            val aesCtr = AesCtrPure(aesKey, Mode.ENCRYPT)
            aesCtr.addData(data)
            return aesCtr.encrypt()
        }
        /**
         * Bulk decryption, returns decrypted data
         */
        fun decrypt(aesKey: InternalAesKey, data: UByteArray, initialCounter: UByteArray? = null): UByteArray {
            val aesCtr = AesCtrPure(aesKey = aesKey, mode = Mode.DECRYPT, initialCounter = initialCounter)
            aesCtr.addData(data)
            return aesCtr.decrypt()
        }

    }

    var currentOutput: UByteArray = ubyteArrayOf()
    var previousEncrypted: UByteArray = ubyteArrayOf()
    val counterStart = if (initialCounter.isNullOrEmpty()) {
        SRNG.getRandomBytes(16)
    } else {
        initialCounter
    }
    var blockCounter = modularCreator.fromBigInteger(BigInteger.fromUByteArray(counterStart, Sign.POSITIVE))

    val output = MutableList<UByteArray>(0) { ubyteArrayOf() }

    var buffer: UByteArray = UByteArray(16) { 0U }
    var bufferCounter = 0

    fun addData(data: UByteArray) {
        //Padding
        when {
            bufferCounter + data.size < BLOCK_BYTES -> appendToBuffer(data, bufferCounter)
            bufferCounter + data.size >= BLOCK_BYTES -> {
                val chunked = data.chunked(BLOCK_BYTES)
                chunked.forEach { chunk ->
                    if (bufferCounter + chunk.size < BLOCK_BYTES) {
                        appendToBuffer(chunk.toUByteArray(), bufferCounter)
                    } else {
                        chunk.toUByteArray().copyInto(
                            destination = buffer,
                            destinationOffset = bufferCounter,
                            startIndex = 0,
                            endIndex = BLOCK_BYTES - bufferCounter
                        )
                        output += consumeBlock(buffer, blockCounter)
                        blockCounter += 1
                        buffer = UByteArray(BLOCK_BYTES) {
                            when (it) {
                                in (0 until (chunk.size - (BLOCK_BYTES - bufferCounter))) -> {
                                    chunk[it + (BLOCK_BYTES - bufferCounter)]
                                }
                                else -> {
                                    0U
                                }
                            }

                        }
                        bufferCounter = chunk.size - (BLOCK_BYTES - bufferCounter)
                    }
                }

            }
        }

    }
    /**
     * Encrypt fed data and return it alongside the randomly chosen initial counter state
     * @return Encrypted data and initial counter state
     */
    fun encrypt(): EncryptedDataAndInitialCounter {
        if (bufferCounter > 0) {
            output += consumeBlock(buffer, blockCounter)
        }
        return EncryptedDataAndInitialCounter(
            output.reversed().foldRight(UByteArray(0) { 0U }) { arrayOfUBytes, acc -> acc + arrayOfUBytes },
            counterStart
        )
    }
    /**
     * Decrypt data
     * @return Decrypted data
     */
    fun decrypt(): UByteArray {
        if (bufferCounter > 0) {
            output += consumeBlock(buffer, blockCounter)
        }
        //JS compiler freaks out here if we don't supply exact type
        val reversed: List<UByteArray> = output.reversed() as List<UByteArray>
        val folded: UByteArray = reversed.foldRight(UByteArray(0) { 0U }) { arrayOfUBytes, acc ->
            acc + arrayOfUBytes
        }
        return folded
    }

    private fun appendToBuffer(array: UByteArray, start: Int) {
        array.copyInto(destination = buffer, destinationOffset = start, startIndex = 0, endIndex = array.size)
        bufferCounter += array.size
    }

    private fun consumeBlock(data: UByteArray, blockCount: ModularBigInteger): UByteArray {
        val blockCountAsByteArray = blockCount.toUByteArray().expandCounterTo16Bytes()
        return when (mode) {
            Mode.ENCRYPT -> {
                AesPure.encrypt(aesKey, blockCountAsByteArray) xor data
            }
            Mode.DECRYPT -> {
                AesPure.encrypt(aesKey, blockCountAsByteArray) xor data
            }
        }

    }

    private fun UByteArray.expandCounterTo16Bytes() : UByteArray {
        return if (this.size < 16) {
            println("Expanding")
            val diff = 16 - this.size
            val pad = UByteArray(diff) { 0U }
            pad + this
        } else {
            this
        }
    }

}

data class EncryptedDataAndInitialCounter(val encryptedData : UByteArray, val initialCounter : UByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as EncryptedDataAndInitialCounter

        if (!encryptedData.contentEquals(other.encryptedData)) return false
        if (!initialCounter.contentEquals(other.initialCounter)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = encryptedData.contentHashCode()
        result = 31 * result + initialCounter.contentHashCode()
        return result
    }
}

