/*
 * Copyright (c) 2019. Ugljesa Jovanovic
 */

package com.ionspin.crypto.blake2b

/**
 * Created by Ugljesa Jovanovic
 * ugljesa.jovanovic@ionspin.com
 * on 15-Jul-2019
 */
fun Array<Byte>.hexColumsPrint() {
    val printout = this.map { it.toString(16) }.chunked(16)
    printout.forEach { println(it.joinToString(separator = " ") { it.toUpperCase() }) }
}

fun Array<UByte>.hexColumsPrint() {
    val printout = this.map { it.toString(16) }.chunked(16)
    printout.forEach { println(it.joinToString(separator = " ") { it.toUpperCase() }) }
}

fun Array<ULong>.hexColumsPrint() {
    val printout = this.map { it.toString(16) }.chunked(3)
    printout.forEach { println(it.joinToString(separator = " ") { it.toUpperCase() }) }
}

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

@ExperimentalUnsignedTypes
infix fun UInt.rotateRight(places: Int): UInt {
    return (this shr places) xor (this shl (32 - places))
}

@ExperimentalUnsignedTypes
infix fun ULong.rotateRight(places: Int): ULong {
    return (this shr places) xor (this shl (64 - places))
}