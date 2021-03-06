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

@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.ionspin.kotlin.crypto.util

//TODO Reorganize this, currently it's just a pile of useful helper functions

val _emitIntArray: IntArray = intArrayOf(1)
/**
 * Created by Ugljesa Jovanovic
 * ugljesa.jovanovic@ionspin.com
 * on 15-Jul-2019
 */

inline fun <reified T> Array<T>.chunked(sliceSize: Int): Array<Array<T>> {
    val last = this.size % sliceSize
    val hasLast = last != 0
    val numberOfSlices = this.size / sliceSize


    val result : MutableList<List<T>> = MutableList<List<T>>(0) { emptyList() }

    for (i in 0 until numberOfSlices) {
        result.add(this.slice(i * sliceSize until (i + 1) * sliceSize))
    }
    if (hasLast) {
        result.add(this.slice(numberOfSlices * sliceSize until this.size))
    }

    return result.map { it.toTypedArray() }.toTypedArray()

}


infix fun UInt.rotateRight(places: Int): UInt {
    return (this shr places) xor (this shl (32 - places))
}


infix fun ULong.rotateRight(places: Int): ULong {
    return (this shr places) xor (this shl (64 - places))
}

infix fun UInt.rotateLeft(places: Int): UInt {
    return (this shl places) xor (this shr (32 - places))
}


infix fun ULong.rotateLeft(places: Int): ULong {
    return (this shl places) xor (this shr (64 - places))
}

infix fun Array<UByte>.xor(other : Array<UByte>) : Array<UByte> {
    if (this.size != other.size) {
        throw RuntimeException("Operands of different sizes are not supported yet")
    }
    return Array(this.size) { this[it] xor other[it] }
}


infix fun UByteArray.xor(other : UByteArray) : UByteArray {
    if (this.size != other.size) {
        throw RuntimeException("Operands of different sizes are not supported yet")
    }
    return UByteArray(this.size) { this[it] xor other[it] }
}

fun UByteArray.xorWithPositions(start: Int, end: Int, other : UByteArray, otherStart: Int) : UByteArray {
    return UByteArray(end - start) { this[start + it] xor other[otherStart + it] }
}

/**
 * Start index is included, end index is excluded
 */
fun UByteArray.xorWithPositionsAndInsertIntoArray(
    start: Int, end: Int,
    other : UByteArray, otherStart: Int,
    targetArray: UByteArray, targetStart : Int
) {
    val length = end - start
    for (i in 0 until length) {
        targetArray[targetStart + i] = this[start + i] xor other[otherStart + i]
    }
}

// UInt / Array utils

fun UInt.toBigEndianUByteArray() : Array<UByte> {
    return Array<UByte> (4) {
        ((this shr (24 - (it * 8))) and 0xFFU).toUByte()
    }
}

fun UInt.toLittleEndianTypedUByteArray() : Array<UByte> {
    return Array<UByte> (4) {
        ((this shr (it * 8)) and 0xFFU).toUByte()
    }
}


fun UInt.toLittleEndianUByteArray() : UByteArray {
    return UByteArray (4) {
        ((this shr (it * 8)) and 0xFFU).toUByte()
    }
}


fun UIntArray.toLittleEndianUByteArray() : UByteArray {
    val result = UByteArray(size * 4)
    for (i in 0 until size) {
        val converted = this[i].toLittleEndianUByteArray()
        result[i * 4] = converted[0]
        result[i * 4 + 1] = converted[1]
        result[i * 4 + 2] = converted[2]
        result[i * 4 + 3] = converted[3]
    }
    return result
}

// UInt / Array utils

fun ULong.toBigEndianUByteArray() : Array<UByte> {
    return Array<UByte> (8) {
        ((this shr (56 - (it * 8))) and 0xFFU).toUByte()
    }
}

fun ULong.toLittleEndianTypedUByteArray() : Array<UByte> {
    return Array<UByte> (8) {
        ((this shr (it * 8)) and 0xFFU).toUByte()
    }
}


fun ULong.toLittleEndianUByteArray() :UByteArray {
    return UByteArray (8) {
        ((this shr (it * 8)) and 0xFFU).toUByte()
    }
}


fun Array<UByte>.fromLittleEndianArrayToULong() : ULong {
    if (this.size > 8) {
        throw RuntimeException("ore than 8 bytes in input, potential overflow")
    }
    var ulong = this.foldIndexed(0UL) { index, acc, uByte -> acc or (uByte.toULong() shl (index * 8))}
    return ulong
}


fun UByteArray.fromLittleEndianArrayToULong() : ULong {
    if (this.size > 8) {
        throw RuntimeException("ore than 8 bytes in input, potential overflow")
    }
    var ulong = this.foldIndexed(0UL) { index, acc, uByte -> acc or (uByte.toULong() shl (index * 8))}
    return ulong
}

fun UByteArray.arrayChunked(sliceSize: Int): List<UByteArray> {
    val last = this.size % sliceSize
    val hasLast = last != 0
    val numberOfSlices = this.size / sliceSize


    val result : MutableList<UByteArray> = MutableList<UByteArray>(0) { ubyteArrayOf() }

    for (i in 0 until numberOfSlices) {
        result.add(this.sliceArray(i * sliceSize until (i + 1) * sliceSize))
    }
    if (hasLast) {
        result.add(this.sliceArray(numberOfSlices * sliceSize until this.size))
    }

    return result
}



fun Array<UByte>.fromBigEndianArrayToULong() : ULong {
    if (this.size > 8) {
        throw RuntimeException("ore than 8 bytes in input, potential overflow")
    }
    var ulong = this.foldIndexed(0UL) {
            index, acc, uByte ->
        val res = acc or (uByte.toULong() shl (56 - (index * 8)))
        res

    }
    return ulong
}


fun Array<UByte>.fromLittleEndianArrayToUInt() : UInt {
    if (this.size > 4) {
        throw RuntimeException("ore than 8 bytes in input, potential overflow")
    }
    var uint = this.foldIndexed(0U) { index, acc, uByte -> acc or (uByte.toUInt() shl (index * 8))}
    return uint
}


fun UByteArray.fromLittleEndianArrayToUInt() : UInt {
    if (this.size > 4) {
        throw RuntimeException("ore than 8 bytes in input, potential overflow")
    }
    var uint = this.foldIndexed(0U) { index, acc, uByte -> acc or (uByte.toUInt() shl (index * 8))}
    return uint
}

fun UByteArray.fromLittleEndianArrayToUIntWithPosition(position: Int) : UInt{
    var uint = 0U
    for (i in 0 until 4) {
        uint = uint or (this[position + i].toUInt() shl (i * 8))
    }
    return uint
}

fun UByteArray.fromBigEndianArrayToUInt() : UInt{
    var uint = 0U
    for (i in 0 until 4) {
        uint = uint shl 8 or (this[i].toUInt())
    }
    return uint
}

fun UByteArray.fromBigEndianArrayToUIntWithPosition(position: Int) : UInt{
    var uint = 0U
    for (i in 0 until 4) {
        uint = uint shl 8 or (this[position + i].toUInt())
    }
    return uint
}

fun UByteArray.insertUIntAtPositionAsLittleEndian(position: Int, value: UInt) {
    for (i in position until position + 4) {
        this[i] = ((value shr (i * 8)) and 0xFFU).toUByte()
    }
}

fun UByteArray.insertUIntAtPositionAsBigEndian(position: Int, value: UInt) {
    for (i in position until position + 4) {
        this[i] = ((value shr (24 - i * 8)) and 0xFFU).toUByte()
    }
}

fun UByteArray.fromLittleEndianToUInt() : UIntArray {
    if (size % 4 != 0) {
        throw RuntimeException("Invalid size (not divisible by 4)")
    }
    return UIntArray(size / 4) {
        fromLittleEndianArrayToUIntWithPosition(it * 4)
    }
}


fun UByteArray.fromBigEndianToUInt() : UIntArray {
    if (size % 4 != 0) {
        throw RuntimeException("Invalid size (not divisible by 4)")
    }
    return UIntArray(size / 4) {
        fromBigEndianArrayToUIntWithPosition(it * 4)
    }
}

fun Array<UByte>.fromBigEndianArrayToUInt() : UInt {
    if (this.size > 4) {
        throw RuntimeException("ore than 8 bytes in input, potential overflow")
    }
    var uint = this.foldIndexed(0U) { index, acc, uByte -> acc or (uByte.toUInt() shl (24 - (index * 8))) }
    return uint
}


operator fun UInt.plus(other : UByteArray) : UByteArray {
    return this.toLittleEndianUByteArray() + other
}

//AES Flatten
fun Collection<UByteArray>.flattenToUByteArray(): UByteArray {
    val result = UByteArray(sumBy { it.size })
    var position = 0
    for (element in this) {
        element.forEach { uByte ->
            result[position] = uByte
            position++
        }
    }
    return result
}
