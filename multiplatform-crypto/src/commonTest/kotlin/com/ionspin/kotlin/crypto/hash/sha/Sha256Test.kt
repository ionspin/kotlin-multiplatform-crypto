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

package com.ionspin.kotlin.crypto.hash.sha

import com.ionspin.kotlin.crypto.hash.encodeToUByteArray
import com.ionspin.kotlin.crypto.util.hexStringToUByteArray
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Created by Ugljesa Jovanovic
 * ugljesa.jovanovic@ionspin.com
 * on 17-Jul-2019
 */

class Sha256Test {


    @Test
    fun testWellKnownValue() {

        val result = Sha256Pure.digest("abc".encodeToUByteArray())
        val expectedResult = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"
        assertTrue {
            result.contentEquals(expectedResult.hexStringToUByteArray())
        }


    }


    @Test
    fun testWellKnownDoubleBlock() {

        val resultDoubleBlock = Sha256Pure.digest("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".encodeToUByteArray())
        val expectedResultForDoubleBlock = "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1"
        assertTrue {
            resultDoubleBlock.contentEquals(expectedResultForDoubleBlock.hexStringToUByteArray())
        }
    }


    @Test
    fun testWellKnown3() { //It's good that I'm consistent with names.


        val resultDoubleBlock = Sha256Pure.digest("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu".encodeToUByteArray())
        println(resultDoubleBlock.map{ it.toString(16)}.joinToString(separator = ""))
        val expectedResultForDoubleBlock = "cf5b16a778af8380036ce59e7b0492370b249b11e8f07a51afac45037afee9d1"
        assertTrue {
            resultDoubleBlock.contentEquals(expectedResultForDoubleBlock.hexStringToUByteArray())
        }
    }


    @Test
    fun testWellKnownLong() {
        val inputBuilder = StringBuilder()
        for (i in 0 until 1000000) {
            inputBuilder.append("a")
        }
        val resultDoubleBlock = Sha256Pure.digest(inputMessage = (inputBuilder.toString()).encodeToByteArray().map { it.toUByte() }.toUByteArray())
        val expectedResultForDoubleBlock = "cdc76e5c9914fb9281a1c7e284d73e67f1809a48a497200e046d39ccc7112cd0"
        assertTrue {
            resultDoubleBlock.contentEquals(expectedResultForDoubleBlock.hexStringToUByteArray())
        }
    }
}
