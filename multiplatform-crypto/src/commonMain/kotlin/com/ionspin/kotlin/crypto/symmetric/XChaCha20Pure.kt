package com.ionspin.kotlin.crypto.symmetric

import com.ionspin.kotlin.crypto.util.fromLittleEndianArrayToUIntWithPosition
import com.ionspin.kotlin.crypto.util.overwriteWithZeroes
import com.ionspin.kotlin.crypto.util.xorWithPositionsAndInsertIntoArray

/**
 * Created by Ugljesa Jovanovic
 * ugljesa.jovanovic@ionspin.com
 * on 14-Jun-2020
 */
class XChaCha20Pure(key: UByteArray, nonce: UByteArray, initialCounter: UInt = 0U) {
    companion object {

        fun hChacha(key: UByteArray, nonce: UByteArray) : UIntArray {
            val state = UIntArray(16) {
                when (it) {
                    0 -> ChaCha20Pure.sigma0_32
                    1 -> ChaCha20Pure.sigma1_32
                    2 -> ChaCha20Pure.sigma2_32
                    3 -> ChaCha20Pure.sigma3_32
                    4 -> key.fromLittleEndianArrayToUIntWithPosition(0)
                    5 -> key.fromLittleEndianArrayToUIntWithPosition(4)
                    6 -> key.fromLittleEndianArrayToUIntWithPosition(8)
                    7 -> key.fromLittleEndianArrayToUIntWithPosition(12)
                    8 -> key.fromLittleEndianArrayToUIntWithPosition(16)
                    9 -> key.fromLittleEndianArrayToUIntWithPosition(20)
                    10 -> key.fromLittleEndianArrayToUIntWithPosition(24)
                    11 -> key.fromLittleEndianArrayToUIntWithPosition(28)
                    12 -> nonce.fromLittleEndianArrayToUIntWithPosition(0)
                    13 -> nonce.fromLittleEndianArrayToUIntWithPosition(4)
                    14 -> nonce.fromLittleEndianArrayToUIntWithPosition(8)
                    15 -> nonce.fromLittleEndianArrayToUIntWithPosition(12)
                    else -> 0U
                }
            }
            for (i in 0 until 10) {
                ChaCha20Pure.doubleRound(state)
            }

            val result = UIntArray(8) {
                when (it) {
                    0 -> state[0]
                    1 -> state[1]
                    2 -> state[2]
                    3 -> state[3]
                    4 -> state[12]
                    5 -> state[13]
                    6 -> state[14]
                    7 -> state[15]
                    else -> throw RuntimeException("Invalid index $it")
                }
            }
            return result

        }

        fun xorWithKeystream(key: UByteArray, nonce: UByteArray, message: UByteArray, initialCounter: UInt = 0U): UByteArray {

            val ciphertext = UByteArray(message.size)
            val hChaChaKey = hChacha(key, nonce)
            val state = UIntArray(16) {
                when (it) {
                    0 -> ChaCha20Pure.sigma0_32
                    1 -> ChaCha20Pure.sigma1_32
                    2 -> ChaCha20Pure.sigma2_32
                    3 -> ChaCha20Pure.sigma3_32
                    4 -> hChaChaKey[0]
                    5 -> hChaChaKey[1]
                    6 -> hChaChaKey[2]
                    7 -> hChaChaKey[3]
                    8 -> hChaChaKey[4]
                    9 -> hChaChaKey[5]
                    10 -> hChaChaKey[6]
                    11 -> hChaChaKey[7]
                    12 -> initialCounter
                    13 -> 0U
                    14 -> nonce.fromLittleEndianArrayToUIntWithPosition(16)
                    15 -> nonce.fromLittleEndianArrayToUIntWithPosition(20)
                    else -> 0U
                }
            }
            hChaChaKey.overwriteWithZeroes()
            val blocks = message.size / 64
            val remainder = message.size % 64
            for (i in 0 until blocks) {
                ChaCha20Pure.hash(state).xorWithPositionsAndInsertIntoArray(0, 64, message, i * 64, ciphertext, i * 64)
                state[12] += 1U
                if (state[12] == 0U) {
                    state[13] += 1U
                }
            }
            ChaCha20Pure.hash(state).xorWithPositionsAndInsertIntoArray(
                0, remainder,
                message, blocks * 64,
                ciphertext, blocks * 64
            )
            state.overwriteWithZeroes()
            return ciphertext
        }

    }

    val state: UIntArray
    val keystreamBuffer = UByteArray(64) { 0U}
    var keystreamRemainingCounter = 0
    var processedBytesSoFar = 0

    init {
        val hChaChaKey = hChacha(key, nonce)
        state = UIntArray(16) {
            when (it) {
                0 -> ChaCha20Pure.sigma0_32
                1 -> ChaCha20Pure.sigma1_32
                2 -> ChaCha20Pure.sigma2_32
                3 -> ChaCha20Pure.sigma3_32
                4 -> hChaChaKey[0]
                5 -> hChaChaKey[1]
                6 -> hChaChaKey[2]
                7 -> hChaChaKey[3]
                8 -> hChaChaKey[4]
                9 -> hChaChaKey[5]
                10 -> hChaChaKey[6]
                11 -> hChaChaKey[7]
                12 -> initialCounter
                13 -> 0U
                14 -> nonce.fromLittleEndianArrayToUIntWithPosition(16)
                15 -> nonce.fromLittleEndianArrayToUIntWithPosition(20)
                else -> 0U
            }
        }
        hChaChaKey.overwriteWithZeroes()
    }

    fun xorWithKeystream(data: UByteArray) : UByteArray {
        val ciphertext = UByteArray(data.size) { 0U }
        //First use remaining keystream
        var processedBytes = 0
        if (data.size > keystreamRemainingCounter) {
            keystreamBuffer.xorWithPositionsAndInsertIntoArray(
                64 - keystreamRemainingCounter, 64,
                data, 0,
                ciphertext, 0
            )
            processedBytes += keystreamRemainingCounter
            keystreamRemainingCounter = 0
        }
        //Then do full blocks
        var remainingBytes = data.size - processedBytes
        for (i in 0 until remainingBytes / 64) {
            ChaCha20Pure.hash(state).xorWithPositionsAndInsertIntoArray(
                0, 64,
                data, processedBytes,
                ciphertext, processedBytes
            )
            state[12] += 1U
            if (state[12] == 0U) {
                state[13] += 1U
            }
            processedBytes += 64
        }
        //Then do the remainder that didn't fit into 64byte blocks
        remainingBytes = data.size - processedBytes
        ChaCha20Pure.hash(state).copyInto(keystreamBuffer)
        state[12] += 1U
        if (state[12] == 0U) {
            state[13] += 1U
        }
        keystreamBuffer.xorWithPositionsAndInsertIntoArray(
            0, remainingBytes,
            data, processedBytes,
            ciphertext, processedBytes
        )
        keystreamRemainingCounter = 64 - remainingBytes
        processedBytesSoFar += data.size
        return ciphertext
    }

}
