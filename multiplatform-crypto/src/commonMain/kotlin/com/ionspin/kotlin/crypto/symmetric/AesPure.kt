package com.ionspin.kotlin.crypto.symmetric

import com.ionspin.kotlin.crypto.util.flattenToUByteArray

/**
 * Created by Ugljesa Jovanovic (jovanovic.ugljesa@gmail.com) on 07/Sep/2019
 */

internal class AesPure internal constructor(val aesKey: InternalAesKey, val input: UByteArray) {
    companion object {
        private val debug = false

        private val sBox: UByteArray =
            ubyteArrayOf(
                // @formatter:off
                0x63U, 0x7cU, 0x77U, 0x7bU, 0xf2U, 0x6bU, 0x6fU, 0xc5U, 0x30U, 0x01U, 0x67U, 0x2bU, 0xfeU, 0xd7U, 0xabU, 0x76U,
                0xcaU, 0x82U, 0xc9U, 0x7dU, 0xfaU, 0x59U, 0x47U, 0xf0U, 0xadU, 0xd4U, 0xa2U, 0xafU, 0x9cU, 0xa4U, 0x72U, 0xc0U,
                0xb7U, 0xfdU, 0x93U, 0x26U, 0x36U, 0x3fU, 0xf7U, 0xccU, 0x34U, 0xa5U, 0xe5U, 0xf1U, 0x71U, 0xd8U, 0x31U, 0x15U,
                0x04U, 0xc7U, 0x23U, 0xc3U, 0x18U, 0x96U, 0x05U, 0x9aU, 0x07U, 0x12U, 0x80U, 0xe2U, 0xebU, 0x27U, 0xb2U, 0x75U,
                0x09U, 0x83U, 0x2cU, 0x1aU, 0x1bU, 0x6eU, 0x5aU, 0xa0U, 0x52U, 0x3bU, 0xd6U, 0xb3U, 0x29U, 0xe3U, 0x2fU, 0x84U,
                0x53U, 0xd1U, 0x00U, 0xedU, 0x20U, 0xfcU, 0xb1U, 0x5bU, 0x6aU, 0xcbU, 0xbeU, 0x39U, 0x4aU, 0x4cU, 0x58U, 0xcfU,
                0xd0U, 0xefU, 0xaaU, 0xfbU, 0x43U, 0x4dU, 0x33U, 0x85U, 0x45U, 0xf9U, 0x02U, 0x7fU, 0x50U, 0x3cU, 0x9fU, 0xa8U,
                0x51U, 0xa3U, 0x40U, 0x8fU, 0x92U, 0x9dU, 0x38U, 0xf5U, 0xbcU, 0xb6U, 0xdaU, 0x21U, 0x10U, 0xffU, 0xf3U, 0xd2U,
                0xcdU, 0x0cU, 0x13U, 0xecU, 0x5fU, 0x97U, 0x44U, 0x17U, 0xc4U, 0xa7U, 0x7eU, 0x3dU, 0x64U, 0x5dU, 0x19U, 0x73U,
                0x60U, 0x81U, 0x4fU, 0xdcU, 0x22U, 0x2aU, 0x90U, 0x88U, 0x46U, 0xeeU, 0xb8U, 0x14U, 0xdeU, 0x5eU, 0x0bU, 0xdbU,
                0xe0U, 0x32U, 0x3aU, 0x0aU, 0x49U, 0x06U, 0x24U, 0x5cU, 0xc2U, 0xd3U, 0xacU, 0x62U, 0x91U, 0x95U, 0xe4U, 0x79U,
                0xe7U, 0xc8U, 0x37U, 0x6dU, 0x8dU, 0xd5U, 0x4eU, 0xa9U, 0x6cU, 0x56U, 0xf4U, 0xeaU, 0x65U, 0x7aU, 0xaeU, 0x08U,
                0xbaU, 0x78U, 0x25U, 0x2eU, 0x1cU, 0xa6U, 0xb4U, 0xc6U, 0xe8U, 0xddU, 0x74U, 0x1fU, 0x4bU, 0xbdU, 0x8bU, 0x8aU,
                0x70U, 0x3eU, 0xb5U, 0x66U, 0x48U, 0x03U, 0xf6U, 0x0eU, 0x61U, 0x35U, 0x57U, 0xb9U, 0x86U, 0xc1U, 0x1dU, 0x9eU,
                0xe1U, 0xf8U, 0x98U, 0x11U, 0x69U, 0xd9U, 0x8eU, 0x94U, 0x9bU, 0x1eU, 0x87U, 0xe9U, 0xceU, 0x55U, 0x28U, 0xdfU,
                0x8cU, 0xa1U, 0x89U, 0x0dU, 0xbfU, 0xe6U, 0x42U, 0x68U, 0x41U, 0x99U, 0x2dU, 0x0fU, 0xb0U, 0x54U, 0xbbU, 0x16U
                    // @formatter:on
            )

        private val inverseSBox: UByteArray =
            ubyteArrayOf(
                // @formatter:off
                0x52U, 0x09U, 0x6aU, 0xd5U, 0x30U, 0x36U, 0xa5U, 0x38U, 0xbfU, 0x40U, 0xa3U, 0x9eU, 0x81U, 0xf3U, 0xd7U, 0xfbU,
                0x7cU, 0xe3U, 0x39U, 0x82U, 0x9bU, 0x2fU, 0xffU, 0x87U, 0x34U, 0x8eU, 0x43U, 0x44U, 0xc4U, 0xdeU, 0xe9U, 0xcbU,
                0x54U, 0x7bU, 0x94U, 0x32U, 0xa6U, 0xc2U, 0x23U, 0x3dU, 0xeeU, 0x4cU, 0x95U, 0x0bU, 0x42U, 0xfaU, 0xc3U, 0x4eU,
                0x08U, 0x2eU, 0xa1U, 0x66U, 0x28U, 0xd9U, 0x24U, 0xb2U, 0x76U, 0x5bU, 0xa2U, 0x49U, 0x6dU, 0x8bU, 0xd1U, 0x25U,
                0x72U, 0xf8U, 0xf6U, 0x64U, 0x86U, 0x68U, 0x98U, 0x16U, 0xd4U, 0xa4U, 0x5cU, 0xccU, 0x5dU, 0x65U, 0xb6U, 0x92U,
                0x6cU, 0x70U, 0x48U, 0x50U, 0xfdU, 0xedU, 0xb9U, 0xdaU, 0x5eU, 0x15U, 0x46U, 0x57U, 0xa7U, 0x8dU, 0x9dU, 0x84U,
                0x90U, 0xd8U, 0xabU, 0x00U, 0x8cU, 0xbcU, 0xd3U, 0x0aU, 0xf7U, 0xe4U, 0x58U, 0x05U, 0xb8U, 0xb3U, 0x45U, 0x06U,
                0xd0U, 0x2cU, 0x1eU, 0x8fU, 0xcaU, 0x3fU, 0x0fU, 0x02U, 0xc1U, 0xafU, 0xbdU, 0x03U, 0x01U, 0x13U, 0x8aU, 0x6bU,
                0x3aU, 0x91U, 0x11U, 0x41U, 0x4fU, 0x67U, 0xdcU, 0xeaU, 0x97U, 0xf2U, 0xcfU, 0xceU, 0xf0U, 0xb4U, 0xe6U, 0x73U,
                0x96U, 0xacU, 0x74U, 0x22U, 0xe7U, 0xadU, 0x35U, 0x85U, 0xe2U, 0xf9U, 0x37U, 0xe8U, 0x1cU, 0x75U, 0xdfU, 0x6eU,
                0x47U, 0xf1U, 0x1aU, 0x71U, 0x1dU, 0x29U, 0xc5U, 0x89U, 0x6fU, 0xb7U, 0x62U, 0x0eU, 0xaaU, 0x18U, 0xbeU, 0x1bU,
                0xfcU, 0x56U, 0x3eU, 0x4bU, 0xc6U, 0xd2U, 0x79U, 0x20U, 0x9aU, 0xdbU, 0xc0U, 0xfeU, 0x78U, 0xcdU, 0x5aU, 0xf4U,
                0x1fU, 0xddU, 0xa8U, 0x33U, 0x88U, 0x07U, 0xc7U, 0x31U, 0xb1U, 0x12U, 0x10U, 0x59U, 0x27U, 0x80U, 0xecU, 0x5fU,
                0x60U, 0x51U, 0x7fU, 0xa9U, 0x19U, 0xb5U, 0x4aU, 0x0dU, 0x2dU, 0xe5U, 0x7aU, 0x9fU, 0x93U, 0xc9U, 0x9cU, 0xefU,
                0xa0U, 0xe0U, 0x3bU, 0x4dU, 0xaeU, 0x2aU, 0xf5U, 0xb0U, 0xc8U, 0xebU, 0xbbU, 0x3cU, 0x83U, 0x53U, 0x99U, 0x61U,
                0x17U, 0x2bU, 0x04U, 0x7eU, 0xbaU, 0x77U, 0xd6U, 0x26U, 0xe1U, 0x69U, 0x14U, 0x63U, 0x55U, 0x21U, 0x0cU, 0x7dU
                // @formatter:on
            )

        val rcon: UByteArray = ubyteArrayOf(0x8DU, 0x01U, 0x02U, 0x04U, 0x08U, 0x10U, 0x20U, 0x40U, 0x80U, 0x1BU, 0x36U)

        fun encrypt(aesKey: InternalAesKey, input: UByteArray): UByteArray {
            return AesPure(aesKey, input).encrypt()
        }

        fun decrypt(aesKey: InternalAesKey, input: UByteArray): UByteArray {
            return AesPure(aesKey, input).decrypt()
        }

    }

    val state: Array<UByteArray> = (0 until 4).map { outerCounter ->
        UByteArray(4) { innerCounter -> input[innerCounter * 4 + outerCounter] }
    }.toTypedArray()

    val numberOfRounds = when (aesKey) {
        is InternalAesKey.Aes128Key -> 10
        is InternalAesKey.Aes192Key -> 12
        is InternalAesKey.Aes256Key -> 14
    }

    val expandedKey: Array<UByteArray> = expandKey()


    var round = 0
    var completed : Boolean = false
        private set

    fun subBytes() {
        state.forEachIndexed { indexRow, row ->
            row.forEachIndexed { indexColumn, element ->
                state[indexRow][indexColumn] = getSBoxValue(element)
            }
        }
    }

    fun getSBoxValue(element: UByte): UByte {
        val firstDigit = (element / 16U).toInt()
        val secondDigit = (element % 16U).toInt()
        return sBox[firstDigit * 16 + secondDigit]
    }

    fun inverseSubBytes() {
        state.forEachIndexed { indexRow, row ->
            row.forEachIndexed { indexColumn, element ->
                state[indexRow][indexColumn] = getInverseSBoxValue(element)
            }
        }
    }

    fun getInverseSBoxValue(element: UByte): UByte {
        val firstDigit = (element / 16U).toInt()
        val secondDigit = (element % 16U).toInt()
        return inverseSBox[firstDigit * 16 + secondDigit]
    }

    fun shiftRows() {
        state[0] = ubyteArrayOf(state[0][0], state[0][1], state[0][2], state[0][3])
        state[1] = ubyteArrayOf(state[1][1], state[1][2], state[1][3], state[1][0])
        state[2] = ubyteArrayOf(state[2][2], state[2][3], state[2][0], state[2][1])
        state[3] = ubyteArrayOf(state[3][3], state[3][0], state[3][1], state[3][2])
    }

    fun inversShiftRows() {
        state[0] = ubyteArrayOf(state[0][0], state[0][1], state[0][2], state[0][3])
        state[1] = ubyteArrayOf(state[1][3], state[1][0], state[1][1], state[1][2])
        state[2] = ubyteArrayOf(state[2][2], state[2][3], state[2][0], state[2][1])
        state[3] = ubyteArrayOf(state[3][1], state[3][2], state[3][3], state[3][0])
    }

    fun mixColumns() {
        val stateMixed: Array<UByteArray> = (0 until 4).map {
            UByteArray(4) { 0U }
        }.toTypedArray()
        for (c in 0..3) {

            stateMixed[0][c] = (2U gfm state[0][c]) xor (3U gfm state[1][c]) xor state[2][c] xor state[3][c]
            stateMixed[1][c] = state[0][c] xor (2U gfm state[1][c]) xor (3U gfm state[2][c]) xor state[3][c]
            stateMixed[2][c] = state[0][c] xor state[1][c] xor (2U gfm state[2][c]) xor (3U gfm state[3][c])
            stateMixed[3][c] = 3U gfm state[0][c] xor state[1][c] xor state[2][c] xor (2U gfm state[3][c])
        }
        stateMixed.copyInto(state)
    }

    fun inverseMixColumns() {
        val stateMixed: Array<UByteArray> = (0 until 4).map {
            UByteArray(4) { 0U }
        }.toTypedArray()
        for (c in 0..3) {
            stateMixed[0][c] =
                (0x0eU gfm state[0][c]) xor (0x0bU gfm state[1][c]) xor (0x0dU gfm state[2][c]) xor (0x09U gfm state[3][c])
            stateMixed[1][c] =
                (0x09U gfm state[0][c]) xor (0x0eU gfm state[1][c]) xor (0x0bU gfm state[2][c]) xor (0x0dU gfm state[3][c])
            stateMixed[2][c] =
                (0x0dU gfm state[0][c]) xor (0x09U gfm state[1][c]) xor (0x0eU gfm state[2][c]) xor (0x0bU gfm state[3][c])
            stateMixed[3][c] =
                (0x0bU gfm state[0][c]) xor (0x0dU gfm state[1][c]) xor (0x09U gfm state[2][c]) xor (0x0eU gfm state[3][c])
        }
        stateMixed.copyInto(state)
    }

    fun galoisFieldAdd(first: UByte, second: UByte): UByte {
        return first xor second
    }

    fun galoisFieldMultiply(first: UByte, second: UByte): UByte {
        var result: UInt = 0U
        var firstInt = first.toUInt()
        var secondInt = second.toUInt()
        var carry: UInt = 0U
        for (i in 0..7) {
            if (secondInt and 0x01U == 1U) {
                result = result xor firstInt
            }
            carry = firstInt and 0x80U
            firstInt = firstInt shl 1
            if (carry == 0x80U) {
                firstInt = firstInt xor 0x001BU
            }
            secondInt = secondInt shr 1
            firstInt = firstInt and 0xFFU
        }
        return result.toUByte()
    }

    fun addRoundKey() {

        for (i in 0 until 4) {
            state[0][i] = state[0][i] xor expandedKey[round * 4 + i][0]
            state[1][i] = state[1][i] xor expandedKey[round * 4 + i][1]
            state[2][i] = state[2][i] xor expandedKey[round * 4 + i][2]
            state[3][i] = state[3][i] xor expandedKey[round * 4 + i][3]
        }
        round++
    }

    fun inverseAddRoundKey() {
        for (i in 0 until 4) {
            state[0][i] = state[0][i] xor expandedKey[round * 4 + i][0]
            state[1][i] = state[1][i] xor expandedKey[round * 4 + i][1]
            state[2][i] = state[2][i] xor expandedKey[round * 4 + i][2]
            state[3][i] = state[3][i] xor expandedKey[round * 4 + i][3]
        }
        round--
    }

    infix fun UInt.gfm(second: UByte): UByte {
        return galoisFieldMultiply(this.toUByte(), second)
    }

    fun expandKey(): Array<UByteArray> {
        val expandedKey = (0 until 4 * (numberOfRounds + 1)).map {
            UByteArray(4) { 0U }
        }.toTypedArray()
        // First round
        for (i in 0 until aesKey.numberOf32BitWords) {
            expandedKey[i][0] = aesKey.keyArray[i * 4 + 0]
            expandedKey[i][1] = aesKey.keyArray[i * 4 + 1]
            expandedKey[i][2] = aesKey.keyArray[i * 4 + 2]
            expandedKey[i][3] = aesKey.keyArray[i * 4 + 3]
        }

        for (i in aesKey.numberOf32BitWords until 4 * (numberOfRounds + 1)) {
            val temp = expandedKey[i - 1].copyOf()
            if (i % aesKey.numberOf32BitWords == 0) {
                //RotWord
                val tempTemp = temp[0]
                temp[0] = temp[1]
                temp[1] = temp[2]
                temp[2] = temp[3]
                temp[3] = tempTemp

                //SubWord
                temp[0] = getSBoxValue(temp[0])
                temp[1] = getSBoxValue(temp[1])
                temp[2] = getSBoxValue(temp[2])
                temp[3] = getSBoxValue(temp[3])

                temp[0] = temp[0] xor rcon[i / aesKey.numberOf32BitWords]

            } else if (aesKey is InternalAesKey.Aes256Key && i % aesKey.numberOf32BitWords == 4) {
                temp[0] = getSBoxValue(temp[0])
                temp[1] = getSBoxValue(temp[1])
                temp[2] = getSBoxValue(temp[2])
                temp[3] = getSBoxValue(temp[3])
            }
            expandedKey[i] = expandedKey[i - aesKey.numberOf32BitWords].mapIndexed { index, it ->
                it xor temp[index]
            }.toUByteArray()
            clearArray(temp)
        }
        return expandedKey
    }

    fun encrypt(): UByteArray {
        if (completed) {
            throw RuntimeException("Encrypt can only be called once per Aes instance, since the state is cleared at the " +
                    "end of the operation")
        }
        printState()
        addRoundKey()
        printState()
        for (i in 0 until numberOfRounds - 1) {
            subBytes()
            printState()
            shiftRows()
            printState()
            mixColumns()
            printState()
            addRoundKey()
            printState()
        }

        subBytes()
        printState()
        shiftRows()
        printState()
        addRoundKey()
        printState()
        val transposedMatrix = (0 until 4).map { outerCounter ->
            UByteArray(4) { 0U }
        }
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                transposedMatrix[i][j] = state[j][i]
            }
        }
        state.forEach { clearArray(it) }
        completed = true
        return transposedMatrix.flattenToUByteArray()
    }

    fun decrypt(): UByteArray {
        if (completed) {
            throw RuntimeException("Decrypt can only be called once per Aes instance, since the state is cleared at the " +
                    "end of the operation")
        }
        round = numberOfRounds
        printState()
        inverseAddRoundKey()
        printState()
        for (i in 0 until numberOfRounds - 1) {
            inversShiftRows()
            printState()
            inverseSubBytes()
            printState()
            inverseAddRoundKey()
            printState()
            inverseMixColumns()
            printState()
        }

        inversShiftRows()
        printState()
        inverseSubBytes()
        printState()
        inverseAddRoundKey()
        printState()

        val transposedMatrix =  (0 until 4).map { outerCounter ->
            UByteArray(4) { 0U }
        }
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                transposedMatrix[i][j] = state[j][i]
            }
        }
        state.forEach { clearArray(it) }
        completed = true
        return transposedMatrix.flattenToUByteArray()
    }

    private fun clearArray(array : UByteArray) {
        array.indices.forEach { array[it] = 0U }
    }



    private fun printState() {
        if (!debug) {
            return
        }
        println()
        state.forEach {
            println(it.joinToString(separator = " ") { it.toString(16) })
        }
    }

    private fun printState(specific : List<UByteArray>) {
        if (!debug) {
            return
        }
        println()
        specific.forEach {
            println(it.joinToString(separator = " ") { it.toString(16) })
        }
    }


}




