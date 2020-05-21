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

@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

package com.ionspin.kotlin.crypto.hash.argon

import com.ionspin.kotlin.crypto.keyderivation.argon2.Argon2Utils
import com.ionspin.kotlin.crypto.util.hexColumsPrint
import kotlin.random.Random
import kotlin.random.nextUBytes
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Created by Ugljesa Jovanovic
 * ugljesa.jovanovic@ionspin.com
 * on 21-May-2020
 */
class Argon2Test {
    val seededRandom = Random(1L)
    val randomBlock = seededRandom.nextUBytes(1024)
    val randomBlock2 = seededRandom.nextUBytes(1024)
    val randomBlock3 = seededRandom.nextUBytes(1024)

    @Test
    fun mixRoundTest() {
        val input = ubyteArrayOf(
            0x00U, 0x01U, 0x02U, 0x03U, 0x04U, 0x05U, 0x06U, 0x07U,
            0x08U, 0x09U, 0x0aU, 0x0bU, 0x0cU, 0x0dU, 0x0eU, 0x0fU,
            0x10U, 0x11U, 0x12U, 0x13U, 0x14U, 0x15U, 0x16U, 0x17U,
            0x18U, 0x19U, 0x1aU, 0x1bU, 0x1cU, 0x1dU, 0x1eU, 0x1fU,
            0x20U, 0x21U, 0x22U, 0x23U, 0x24U, 0x25U, 0x26U, 0x27U,
            0x28U, 0x29U, 0x2aU, 0x2bU, 0x2cU, 0x2dU, 0x2eU, 0x2fU,
            0x30U, 0x31U, 0x32U, 0x33U, 0x34U, 0x35U, 0x36U, 0x37U,
            0x38U, 0x39U, 0x3aU, 0x3bU, 0x3cU, 0x3dU, 0x3eU, 0x3fU,
            0x40U, 0x41U, 0x42U, 0x43U, 0x44U, 0x45U, 0x46U, 0x47U,
            0x48U, 0x49U, 0x4aU, 0x4bU, 0x4cU, 0x4dU, 0x4eU, 0x4fU,
            0x50U, 0x51U, 0x52U, 0x53U, 0x54U, 0x55U, 0x56U, 0x57U,
            0x58U, 0x59U, 0x5aU, 0x5bU, 0x5cU, 0x5dU, 0x5eU, 0x5fU,
            0x60U, 0x61U, 0x62U, 0x63U, 0x64U, 0x65U, 0x66U, 0x67U,
            0x68U, 0x69U, 0x6aU, 0x6bU, 0x6cU, 0x6dU, 0x6eU, 0x6fU,
            0x70U, 0x71U, 0x72U, 0x73U, 0x74U, 0x75U, 0x76U, 0x77U,
            0x78U, 0x79U, 0x7aU, 0x7bU, 0x7cU, 0x7dU, 0x7eU, 0x7fU
        )

        val expected = arrayOf(
            16438755999881694465U,
            2631578750119870528U,
            8840261388583117524U,
            13886387434724287670U,
            14214935523062117944U,
            6768869593113706780U,
            12323449447979969623U,
            7512951229622659062U,
            9291745133539598579U,
            11220895723773995914U,
            2509429320847842905U,
            5637172405908834370U,
            8517838221434905893U,
            14206719563334097702U,
            6500029010075826286U,
            16957672821843227543U
        )
        val result = Argon2Utils.mixRound(input)
        assertTrue {
            expected.contentEquals(result)
        }

    }

    @Test
    fun testExtractColumnFromGBlock() {
        val expected = ubyteArrayOf(
            0x1AU, 0x2AU, 0xC5U, 0x23U, 0xB0U, 0x05U, 0xB1U, 0xA4U,
            0x61U, 0x48U, 0x93U, 0xE0U, 0x6DU, 0x33U, 0xB6U, 0xA0U,
            0x63U, 0x13U, 0xFFU, 0xEBU, 0x56U, 0x48U, 0xE7U, 0xD0U,
            0x47U, 0x58U, 0x8EU, 0xD7U, 0xDEU, 0x01U, 0xCFU, 0x96U,
            0xB4U, 0xE0U, 0x2AU, 0xF0U, 0x16U, 0x33U, 0x54U, 0xA9U,
            0xF3U, 0xC3U, 0x98U, 0x2CU, 0xB7U, 0xECU, 0x1AU, 0x66U,
            0xE0U, 0x6CU, 0xA5U, 0x66U, 0x73U, 0xE8U, 0x8AU, 0xF7U,
            0x50U, 0x96U, 0xB8U, 0x16U, 0x47U, 0x41U, 0xA6U, 0x4CU,
            0xAEU, 0x89U, 0xBAU, 0x5DU, 0x3CU, 0x49U, 0x33U, 0x23U,
            0xD3U, 0xFBU, 0xD4U, 0x04U, 0x11U, 0x8FU, 0x98U, 0x10U,
            0xDCU, 0xB7U, 0x06U, 0xE0U, 0x58U, 0xCFU, 0x48U, 0xE3U,
            0x1FU, 0x33U, 0xE6U, 0x66U, 0x20U, 0xD2U, 0x34U, 0x43U,
            0x62U, 0xA0U, 0x02U, 0x7FU, 0xE9U, 0x36U, 0xB3U, 0xB5U,
            0x6BU, 0x07U, 0x8CU, 0xA7U, 0xB1U, 0x11U, 0x28U, 0x6FU,
            0x6BU, 0xD0U, 0x09U, 0x4FU, 0xACU, 0x48U, 0x18U, 0xDCU,
            0x70U, 0xB6U, 0xD6U, 0x27U, 0x69U, 0x50U, 0x97U, 0x87U,
        )
        val extracted = Argon2Utils.extractColumnFromGBlock(randomBlock, 0)
        assertTrue {
            expected.contentEquals(extracted)
        }

    }

    @Test
    fun testCompressionFunctionGWithoutXor() {
        val expectedWithoutXor = ubyteArrayOf(
            0x94U, 0xE8U, 0x52U, 0x79U, 0xA7U, 0xE5U, 0x66U, 0xAAU,
            0xD7U, 0xE1U, 0x8EU, 0x72U, 0x42U, 0xA2U, 0xE1U, 0x50U,
            0x5FU, 0x77U, 0x7DU, 0xCDU, 0xE1U, 0xEFU, 0xAAU, 0xF5U,
            0x29U, 0x94U, 0x29U, 0x17U, 0x9DU, 0x79U, 0xA5U, 0x67U,
            0xD7U, 0x6CU, 0x11U, 0x18U, 0x6FU, 0xE0U, 0xA4U, 0x26U,
            0xCCU, 0xF4U, 0xFDU, 0xD1U, 0xE0U, 0x42U, 0xC1U, 0x6AU,
            0x33U, 0x99U, 0xC4U, 0x27U, 0x33U, 0x67U, 0xDDU, 0xC3U,
            0x57U, 0x81U, 0xF5U, 0xCDU, 0x05U, 0x22U, 0x2DU, 0x5FU,
            0x71U, 0x83U, 0xA9U, 0x54U, 0x1BU, 0x09U, 0x40U, 0x68U,
            0x6DU, 0x78U, 0x0FU, 0x23U, 0xE2U, 0x90U, 0xC1U, 0x2DU,
            0xBFU, 0x44U, 0x26U, 0xADU, 0x8BU, 0x77U, 0x0EU, 0xFBU,
            0x9AU, 0x95U, 0x7AU, 0x8DU, 0xEAU, 0xCAU, 0xA2U, 0x1BU,
            0xBDU, 0xC0U, 0x90U, 0x85U, 0x37U, 0xD9U, 0xF3U, 0x94U,
            0x59U, 0x9EU, 0xB4U, 0xC4U, 0xBCU, 0xC1U, 0x47U, 0x31U,
            0x66U, 0xE4U, 0x66U, 0x0EU, 0x86U, 0x11U, 0x4AU, 0x4CU,
            0x36U, 0xA1U, 0x1CU, 0xDEU, 0xE9U, 0xA4U, 0x03U, 0x1FU,
            0x9CU, 0x7AU, 0x1BU, 0x7AU, 0x8FU, 0x17U, 0xA3U, 0xD4U,
            0x5BU, 0x23U, 0x8AU, 0x55U, 0x53U, 0x86U, 0x64U, 0xEDU,
            0x24U, 0x97U, 0xFAU, 0x70U, 0xD4U, 0xF1U, 0x9AU, 0x10U,
            0xBCU, 0x24U, 0x03U, 0xACU, 0x24U, 0x91U, 0x83U, 0xFBU,
            0x66U, 0x02U, 0xC2U, 0x37U, 0xBEU, 0x63U, 0xCEU, 0x59U,
            0xEAU, 0x02U, 0xAFU, 0x6CU, 0x8DU, 0x14U, 0xECU, 0xFDU,
            0x5BU, 0x0AU, 0x0EU, 0xB6U, 0x51U, 0x2FU, 0x75U, 0xF1U,
            0xA9U, 0x88U, 0xD1U, 0x05U, 0x60U, 0xE6U, 0xEDU, 0x17U,
            0x4AU, 0x03U, 0x1FU, 0xF7U, 0x71U, 0x86U, 0x26U, 0x18U,
            0x7EU, 0xB3U, 0x31U, 0x78U, 0xD8U, 0xB7U, 0x2CU, 0xD0U,
            0x6DU, 0xB5U, 0xAEU, 0xDFU, 0x85U, 0xC6U, 0x87U, 0x9FU,
            0x18U, 0x85U, 0x1BU, 0x22U, 0x19U, 0x15U, 0x00U, 0xF8U,
            0x03U, 0xF7U, 0x48U, 0xA4U, 0xFCU, 0xE3U, 0xE4U, 0x93U,
            0x02U, 0x57U, 0x71U, 0xBCU, 0xF6U, 0x2DU, 0x45U, 0xBEU,
            0x87U, 0xC1U, 0x21U, 0x11U, 0x77U, 0x23U, 0x78U, 0xFCU,
            0x56U, 0xBFU, 0x92U, 0x49U, 0xA1U, 0x51U, 0xE9U, 0x22U,
            0x24U, 0x10U, 0x30U, 0x2EU, 0xE1U, 0xC1U, 0xF7U, 0x96U,
            0xB0U, 0x27U, 0xF7U, 0x6CU, 0x65U, 0x0CU, 0x1BU, 0xE1U,
            0xB2U, 0x12U, 0xD3U, 0xBCU, 0x8BU, 0xEDU, 0x9DU, 0x14U,
            0x81U, 0x82U, 0xB3U, 0x5BU, 0x70U, 0x2CU, 0x65U, 0x54U,
            0xF5U, 0x1FU, 0x1CU, 0x37U, 0xD2U, 0xD1U, 0x89U, 0x35U,
            0x57U, 0x52U, 0xDDU, 0xA7U, 0x02U, 0x81U, 0x14U, 0xF7U,
            0x6CU, 0x4CU, 0xB9U, 0x02U, 0x96U, 0x72U, 0xD2U, 0xFEU,
            0x30U, 0x66U, 0x8CU, 0x16U, 0x50U, 0xB1U, 0x51U, 0xE9U,
            0x48U, 0x48U, 0x83U, 0xA6U, 0x5CU, 0xF9U, 0x98U, 0x59U,
            0xBAU, 0x2DU, 0xE5U, 0x73U, 0xADU, 0xD1U, 0x6AU, 0x7AU,
            0x1FU, 0x6EU, 0xD8U, 0xC9U, 0xEDU, 0x00U, 0x9AU, 0xB1U,
            0x34U, 0x08U, 0x0AU, 0x81U, 0x3EU, 0xCCU, 0x44U, 0xB7U,
            0xD6U, 0xFEU, 0x0FU, 0x5CU, 0x19U, 0xD8U, 0x8FU, 0xEDU,
            0x46U, 0x88U, 0xF5U, 0x5DU, 0x6BU, 0xDAU, 0x4DU, 0x02U,
            0xA9U, 0xFCU, 0x67U, 0xBEU, 0x0CU, 0x09U, 0x38U, 0x98U,
            0x79U, 0x52U, 0x7DU, 0xA6U, 0x24U, 0xA8U, 0x3BU, 0xB1U,
            0xB2U, 0x52U, 0xC8U, 0x83U, 0x6CU, 0x02U, 0xD8U, 0xBDU,
            0x7FU, 0x98U, 0xCCU, 0x65U, 0x01U, 0x90U, 0x6EU, 0x60U,
            0xF8U, 0xB8U, 0x94U, 0xABU, 0x7BU, 0xA9U, 0x5FU, 0xE9U,
            0xDAU, 0xCCU, 0x94U, 0x00U, 0xEDU, 0x8FU, 0x03U, 0xA3U,
            0xCFU, 0xB3U, 0x14U, 0x38U, 0x94U, 0x91U, 0xF3U, 0x61U,
            0xEBU, 0x04U, 0xDEU, 0x9DU, 0x9DU, 0x05U, 0x98U, 0xF3U,
            0x71U, 0xB1U, 0xB3U, 0xB1U, 0x1DU, 0xA3U, 0x55U, 0x47U,
            0xF4U, 0xA3U, 0xF8U, 0x7AU, 0x11U, 0xBBU, 0x0DU, 0x9CU,
            0xB1U, 0x57U, 0xAAU, 0xC2U, 0x3CU, 0x0BU, 0xBEU, 0x77U,
            0xBCU, 0x25U, 0x03U, 0x8BU, 0x7FU, 0x81U, 0xA5U, 0x7EU,
            0xA9U, 0xB4U, 0x3FU, 0x5AU, 0x11U, 0x54U, 0x75U, 0x7DU,
            0x45U, 0x20U, 0x90U, 0x46U, 0x98U, 0x70U, 0xF5U, 0x5BU,
            0xA5U, 0x9DU, 0x46U, 0x63U, 0x62U, 0x3DU, 0x04U, 0x2FU,
            0xB9U, 0x67U, 0x31U, 0xC5U, 0x92U, 0x48U, 0x18U, 0x0FU,
            0xDCU, 0x52U, 0x78U, 0xCBU, 0x7CU, 0x86U, 0x58U, 0xDBU,
            0x0BU, 0x06U, 0xF2U, 0x93U, 0x83U, 0x26U, 0x99U, 0x5DU,
            0xBDU, 0xFBU, 0x87U, 0xC9U, 0x55U, 0x41U, 0xACU, 0x5FU,
            0xA3U, 0xF9U, 0x71U, 0x53U, 0x93U, 0x53U, 0xC5U, 0xE4U,
            0x3FU, 0x78U, 0x31U, 0x8FU, 0x7CU, 0x0BU, 0x77U, 0x84U,
            0x43U, 0xACU, 0x4DU, 0xD1U, 0xC9U, 0x9BU, 0x4BU, 0x60U,
            0x00U, 0xFEU, 0x41U, 0x10U, 0xEFU, 0xDFU, 0x80U, 0x95U,
            0x03U, 0x23U, 0xBEU, 0xF6U, 0x6BU, 0x60U, 0xE5U, 0x6FU,
            0x9EU, 0x6BU, 0x7CU, 0x24U, 0xA7U, 0xC1U, 0xF8U, 0xBBU,
            0x60U, 0x55U, 0x57U, 0x18U, 0x58U, 0x32U, 0x97U, 0xE8U,
            0x53U, 0x71U, 0x05U, 0x59U, 0xE3U, 0x94U, 0xB1U, 0x60U,
            0x8AU, 0x40U, 0x24U, 0xF6U, 0x29U, 0x49U, 0x65U, 0xADU,
            0x65U, 0xE3U, 0xCEU, 0x9FU, 0x4FU, 0x47U, 0xA0U, 0x07U,
            0x2FU, 0x1CU, 0xA7U, 0x3AU, 0x04U, 0x17U, 0x87U, 0x1BU,
            0x06U, 0xC5U, 0x93U, 0x63U, 0x3FU, 0x18U, 0x3BU, 0x8BU,
            0x3EU, 0xD4U, 0xC0U, 0xC9U, 0x7EU, 0xB6U, 0x32U, 0xD3U,
            0xC3U, 0x8BU, 0x55U, 0x2CU, 0x5BU, 0xB7U, 0xEBU, 0x88U,
            0x14U, 0x63U, 0xB8U, 0x3BU, 0xDBU, 0xFFU, 0x49U, 0x44U,
            0x79U, 0xD2U, 0x82U, 0xD8U, 0xA6U, 0x47U, 0x45U, 0xFBU,
            0xF5U, 0x4BU, 0x5AU, 0x43U, 0x19U, 0xD9U, 0xD5U, 0x52U,
            0x04U, 0x0CU, 0x4BU, 0x74U, 0x87U, 0x0DU, 0x11U, 0x8DU,
            0x51U, 0x9DU, 0x66U, 0x68U, 0x41U, 0xE0U, 0xFCU, 0x92U,
            0x24U, 0x7FU, 0x62U, 0xAFU, 0x8BU, 0x53U, 0xDEU, 0x84U,
            0xF0U, 0xA1U, 0x03U, 0xAAU, 0x17U, 0x6FU, 0xD3U, 0x10U,
            0x57U, 0x25U, 0xC6U, 0xA3U, 0x8FU, 0x1AU, 0xD3U, 0xD7U,
            0x0DU, 0x89U, 0x22U, 0x3CU, 0x17U, 0xC6U, 0xF7U, 0x02U,
            0x0CU, 0xD9U, 0x0FU, 0xABU, 0x27U, 0xA6U, 0xE3U, 0x20U,
            0xF4U, 0xB2U, 0x21U, 0x12U, 0x85U, 0xF5U, 0x98U, 0x45U,
            0x16U, 0x24U, 0xF7U, 0x46U, 0x5DU, 0x1FU, 0x41U, 0x9AU,
            0x0EU, 0x25U, 0x4AU, 0x99U, 0x27U, 0x5CU, 0x0BU, 0xC8U,
            0x33U, 0x99U, 0x65U, 0x3CU, 0xC9U, 0xDBU, 0x78U, 0x6DU,
            0x36U, 0xC6U, 0x37U, 0x35U, 0x2EU, 0x97U, 0x04U, 0x03U,
            0x26U, 0x78U, 0x5AU, 0x5AU, 0xD8U, 0xEAU, 0xF7U, 0xF1U,
            0xF9U, 0x22U, 0x9DU, 0xDEU, 0xB7U, 0x50U, 0xFEU, 0x91U,
            0xD4U, 0x5EU, 0xA1U, 0xC2U, 0x1BU, 0xE4U, 0x3FU, 0x19U,
            0x50U, 0x21U, 0x1FU, 0x38U, 0xA1U, 0x11U, 0xB1U, 0x08U,
            0x7DU, 0xA2U, 0xBBU, 0xE5U, 0xECU, 0x1BU, 0x9FU, 0x51U,
            0x77U, 0x6CU, 0xB9U, 0xA8U, 0xC2U, 0x76U, 0xD4U, 0xCBU,
            0x11U, 0x7DU, 0x4AU, 0x92U, 0xE2U, 0x0AU, 0x96U, 0x9AU,
            0x28U, 0x72U, 0xFFU, 0xAAU, 0xF8U, 0xD5U, 0x64U, 0x8AU,
            0x45U, 0x71U, 0xF3U, 0xB5U, 0xD7U, 0x2AU, 0x3BU, 0x7FU,
            0x5CU, 0xFCU, 0x2DU, 0x89U, 0xBCU, 0x64U, 0x2FU, 0x63U,
            0x87U, 0xE6U, 0x57U, 0xECU, 0x06U, 0xE3U, 0xDDU, 0x15U,
            0xB4U, 0x27U, 0xA8U, 0x6CU, 0x37U, 0x54U, 0xD3U, 0x37U,
            0xCFU, 0x4DU, 0x71U, 0x04U, 0x91U, 0x08U, 0x1AU, 0xC6U,
            0xBDU, 0xEBU, 0x86U, 0xC5U, 0x5AU, 0x63U, 0x19U, 0xD5U,
            0xC5U, 0xCBU, 0x82U, 0xC3U, 0x54U, 0x57U, 0xD8U, 0x6CU,
            0x8FU, 0x9AU, 0xF1U, 0x7BU, 0x08U, 0x61U, 0xFCU, 0x96U,
            0x1AU, 0xCEU, 0x8FU, 0x11U, 0xE2U, 0xDDU, 0x96U, 0xA3U,
            0x57U, 0xD7U, 0x0DU, 0x28U, 0x06U, 0x0AU, 0xC5U, 0x1FU,
            0xBBU, 0xC6U, 0x67U, 0xC8U, 0xB0U, 0xA7U, 0xDAU, 0x00U,
            0xC3U, 0x00U, 0x21U, 0xACU, 0xFFU, 0xE9U, 0x4FU, 0xB7U,
            0x9AU, 0xC9U, 0x77U, 0xC3U, 0x96U, 0x6EU, 0x1CU, 0xC4U,
            0x61U, 0xAEU, 0x6FU, 0x55U, 0x0CU, 0xDAU, 0x68U, 0x48U,
            0x97U, 0xEDU, 0x9CU, 0x90U, 0x17U, 0x2AU, 0x2DU, 0xC9U,
            0x2AU, 0x77U, 0x87U, 0xECU, 0x64U, 0xF5U, 0x78U, 0x99U,
            0xA9U, 0xB9U, 0x11U, 0x05U, 0xE9U, 0x7BU, 0x3BU, 0x49U,
            0xAEU, 0x61U, 0x70U, 0x0BU, 0xBFU, 0xB7U, 0x67U, 0xA8U,
            0x9FU, 0x02U, 0x30U, 0xD3U, 0x0BU, 0x14U, 0xF0U, 0x89U,
            0x95U, 0x87U, 0x5BU, 0x04U, 0xF3U, 0x27U, 0xEFU, 0x91U,
            0x4AU, 0xD4U, 0x7FU, 0x9EU, 0x73U, 0x95U, 0xE5U, 0x48U,
            0x7FU, 0xE4U, 0xF6U, 0x92U, 0x85U, 0x47U, 0xA5U, 0xFEU,
            0x12U, 0xD7U, 0xC4U, 0x9EU, 0xCFU, 0x8AU, 0xC2U, 0xD2U,
            0x10U, 0x94U, 0xE5U, 0x58U, 0xBBU, 0xE0U, 0xEEU, 0xD9U,
            0x6AU, 0x59U, 0xD9U, 0xBCU, 0xD5U, 0xCDU, 0xA0U, 0xBCU,
            0xD7U, 0x96U, 0xAAU, 0x23U, 0xA9U, 0x10U, 0x6CU, 0x7EU,

            )
        val resultWithoutXor = Argon2Utils.compressionFunctionG(randomBlock, randomBlock2, randomBlock3, false)
        assertTrue {
            expectedWithoutXor.contentEquals(resultWithoutXor)
        }
    }

    @Test
    fun compressionFunctionGWithXor() {
        val expectedWithXor = ubyteArrayOf(

            0x94U, 0xE8U, 0x52U, 0x79U, 0xA7U, 0xE5U, 0x66U, 0xAAU,
            0xD7U, 0xE1U, 0x8EU, 0x72U, 0x42U, 0xA2U, 0xE1U, 0x50U,
            0x5FU, 0x77U, 0x7DU, 0xCDU, 0xE1U, 0xEFU, 0xAAU, 0xF5U,
            0x29U, 0x94U, 0x29U, 0x17U, 0x9DU, 0x79U, 0xA5U, 0x67U,
            0xD7U, 0x6CU, 0x11U, 0x18U, 0x6FU, 0xE0U, 0xA4U, 0x26U,
            0xCCU, 0xF4U, 0xFDU, 0xD1U, 0xE0U, 0x42U, 0xC1U, 0x6AU,
            0x33U, 0x99U, 0xC4U, 0x27U, 0x33U, 0x67U, 0xDDU, 0xC3U,
            0x57U, 0x81U, 0xF5U, 0xCDU, 0x05U, 0x22U, 0x2DU, 0x5FU,
            0x71U, 0x83U, 0xA9U, 0x54U, 0x1BU, 0x09U, 0x40U, 0x68U,
            0x6DU, 0x78U, 0x0FU, 0x23U, 0xE2U, 0x90U, 0xC1U, 0x2DU,
            0xBFU, 0x44U, 0x26U, 0xADU, 0x8BU, 0x77U, 0x0EU, 0xFBU,
            0x9AU, 0x95U, 0x7AU, 0x8DU, 0xEAU, 0xCAU, 0xA2U, 0x1BU,
            0xBDU, 0xC0U, 0x90U, 0x85U, 0x37U, 0xD9U, 0xF3U, 0x94U,
            0x59U, 0x9EU, 0xB4U, 0xC4U, 0xBCU, 0xC1U, 0x47U, 0x31U,
            0x66U, 0xE4U, 0x66U, 0x0EU, 0x86U, 0x11U, 0x4AU, 0x4CU,
            0x36U, 0xA1U, 0x1CU, 0xDEU, 0xE9U, 0xA4U, 0x03U, 0x1FU,
            0x9CU, 0x7AU, 0x1BU, 0x7AU, 0x8FU, 0x17U, 0xA3U, 0xD4U,
            0x5BU, 0x23U, 0x8AU, 0x55U, 0x53U, 0x86U, 0x64U, 0xEDU,
            0x24U, 0x97U, 0xFAU, 0x70U, 0xD4U, 0xF1U, 0x9AU, 0x10U,
            0xBCU, 0x24U, 0x03U, 0xACU, 0x24U, 0x91U, 0x83U, 0xFBU,
            0x66U, 0x02U, 0xC2U, 0x37U, 0xBEU, 0x63U, 0xCEU, 0x59U,
            0xEAU, 0x02U, 0xAFU, 0x6CU, 0x8DU, 0x14U, 0xECU, 0xFDU,
            0x5BU, 0x0AU, 0x0EU, 0xB6U, 0x51U, 0x2FU, 0x75U, 0xF1U,
            0xA9U, 0x88U, 0xD1U, 0x05U, 0x60U, 0xE6U, 0xEDU, 0x17U,
            0x4AU, 0x03U, 0x1FU, 0xF7U, 0x71U, 0x86U, 0x26U, 0x18U,
            0x7EU, 0xB3U, 0x31U, 0x78U, 0xD8U, 0xB7U, 0x2CU, 0xD0U,
            0x6DU, 0xB5U, 0xAEU, 0xDFU, 0x85U, 0xC6U, 0x87U, 0x9FU,
            0x18U, 0x85U, 0x1BU, 0x22U, 0x19U, 0x15U, 0x00U, 0xF8U,
            0x03U, 0xF7U, 0x48U, 0xA4U, 0xFCU, 0xE3U, 0xE4U, 0x93U,
            0x02U, 0x57U, 0x71U, 0xBCU, 0xF6U, 0x2DU, 0x45U, 0xBEU,
            0x87U, 0xC1U, 0x21U, 0x11U, 0x77U, 0x23U, 0x78U, 0xFCU,
            0x56U, 0xBFU, 0x92U, 0x49U, 0xA1U, 0x51U, 0xE9U, 0x22U,
            0x24U, 0x10U, 0x30U, 0x2EU, 0xE1U, 0xC1U, 0xF7U, 0x96U,
            0xB0U, 0x27U, 0xF7U, 0x6CU, 0x65U, 0x0CU, 0x1BU, 0xE1U,
            0xB2U, 0x12U, 0xD3U, 0xBCU, 0x8BU, 0xEDU, 0x9DU, 0x14U,
            0x81U, 0x82U, 0xB3U, 0x5BU, 0x70U, 0x2CU, 0x65U, 0x54U,
            0xF5U, 0x1FU, 0x1CU, 0x37U, 0xD2U, 0xD1U, 0x89U, 0x35U,
            0x57U, 0x52U, 0xDDU, 0xA7U, 0x02U, 0x81U, 0x14U, 0xF7U,
            0x6CU, 0x4CU, 0xB9U, 0x02U, 0x96U, 0x72U, 0xD2U, 0xFEU,
            0x30U, 0x66U, 0x8CU, 0x16U, 0x50U, 0xB1U, 0x51U, 0xE9U,
            0x48U, 0x48U, 0x83U, 0xA6U, 0x5CU, 0xF9U, 0x98U, 0x59U,
            0xBAU, 0x2DU, 0xE5U, 0x73U, 0xADU, 0xD1U, 0x6AU, 0x7AU,
            0x1FU, 0x6EU, 0xD8U, 0xC9U, 0xEDU, 0x00U, 0x9AU, 0xB1U,
            0x34U, 0x08U, 0x0AU, 0x81U, 0x3EU, 0xCCU, 0x44U, 0xB7U,
            0xD6U, 0xFEU, 0x0FU, 0x5CU, 0x19U, 0xD8U, 0x8FU, 0xEDU,
            0x46U, 0x88U, 0xF5U, 0x5DU, 0x6BU, 0xDAU, 0x4DU, 0x02U,
            0xA9U, 0xFCU, 0x67U, 0xBEU, 0x0CU, 0x09U, 0x38U, 0x98U,
            0x79U, 0x52U, 0x7DU, 0xA6U, 0x24U, 0xA8U, 0x3BU, 0xB1U,
            0xB2U, 0x52U, 0xC8U, 0x83U, 0x6CU, 0x02U, 0xD8U, 0xBDU,
            0x7FU, 0x98U, 0xCCU, 0x65U, 0x01U, 0x90U, 0x6EU, 0x60U,
            0xF8U, 0xB8U, 0x94U, 0xABU, 0x7BU, 0xA9U, 0x5FU, 0xE9U,
            0xDAU, 0xCCU, 0x94U, 0x00U, 0xEDU, 0x8FU, 0x03U, 0xA3U,
            0xCFU, 0xB3U, 0x14U, 0x38U, 0x94U, 0x91U, 0xF3U, 0x61U,
            0xEBU, 0x04U, 0xDEU, 0x9DU, 0x9DU, 0x05U, 0x98U, 0xF3U,
            0x71U, 0xB1U, 0xB3U, 0xB1U, 0x1DU, 0xA3U, 0x55U, 0x47U,
            0xF4U, 0xA3U, 0xF8U, 0x7AU, 0x11U, 0xBBU, 0x0DU, 0x9CU,
            0xB1U, 0x57U, 0xAAU, 0xC2U, 0x3CU, 0x0BU, 0xBEU, 0x77U,
            0xBCU, 0x25U, 0x03U, 0x8BU, 0x7FU, 0x81U, 0xA5U, 0x7EU,
            0xA9U, 0xB4U, 0x3FU, 0x5AU, 0x11U, 0x54U, 0x75U, 0x7DU,
            0x45U, 0x20U, 0x90U, 0x46U, 0x98U, 0x70U, 0xF5U, 0x5BU,
            0xA5U, 0x9DU, 0x46U, 0x63U, 0x62U, 0x3DU, 0x04U, 0x2FU,
            0xB9U, 0x67U, 0x31U, 0xC5U, 0x92U, 0x48U, 0x18U, 0x0FU,
            0xDCU, 0x52U, 0x78U, 0xCBU, 0x7CU, 0x86U, 0x58U, 0xDBU,
            0x0BU, 0x06U, 0xF2U, 0x93U, 0x83U, 0x26U, 0x99U, 0x5DU,
            0xBDU, 0xFBU, 0x87U, 0xC9U, 0x55U, 0x41U, 0xACU, 0x5FU,
            0xA3U, 0xF9U, 0x71U, 0x53U, 0x93U, 0x53U, 0xC5U, 0xE4U,
            0x3FU, 0x78U, 0x31U, 0x8FU, 0x7CU, 0x0BU, 0x77U, 0x84U,
            0x43U, 0xACU, 0x4DU, 0xD1U, 0xC9U, 0x9BU, 0x4BU, 0x60U,
            0x00U, 0xFEU, 0x41U, 0x10U, 0xEFU, 0xDFU, 0x80U, 0x95U,
            0x03U, 0x23U, 0xBEU, 0xF6U, 0x6BU, 0x60U, 0xE5U, 0x6FU,
            0x9EU, 0x6BU, 0x7CU, 0x24U, 0xA7U, 0xC1U, 0xF8U, 0xBBU,
            0x60U, 0x55U, 0x57U, 0x18U, 0x58U, 0x32U, 0x97U, 0xE8U,
            0x53U, 0x71U, 0x05U, 0x59U, 0xE3U, 0x94U, 0xB1U, 0x60U,
            0x8AU, 0x40U, 0x24U, 0xF6U, 0x29U, 0x49U, 0x65U, 0xADU,
            0x65U, 0xE3U, 0xCEU, 0x9FU, 0x4FU, 0x47U, 0xA0U, 0x07U,
            0x2FU, 0x1CU, 0xA7U, 0x3AU, 0x04U, 0x17U, 0x87U, 0x1BU,
            0x06U, 0xC5U, 0x93U, 0x63U, 0x3FU, 0x18U, 0x3BU, 0x8BU,
            0x3EU, 0xD4U, 0xC0U, 0xC9U, 0x7EU, 0xB6U, 0x32U, 0xD3U,
            0xC3U, 0x8BU, 0x55U, 0x2CU, 0x5BU, 0xB7U, 0xEBU, 0x88U,
            0x14U, 0x63U, 0xB8U, 0x3BU, 0xDBU, 0xFFU, 0x49U, 0x44U,
            0x79U, 0xD2U, 0x82U, 0xD8U, 0xA6U, 0x47U, 0x45U, 0xFBU,
            0xF5U, 0x4BU, 0x5AU, 0x43U, 0x19U, 0xD9U, 0xD5U, 0x52U,
            0x04U, 0x0CU, 0x4BU, 0x74U, 0x87U, 0x0DU, 0x11U, 0x8DU,
            0x51U, 0x9DU, 0x66U, 0x68U, 0x41U, 0xE0U, 0xFCU, 0x92U,
            0x24U, 0x7FU, 0x62U, 0xAFU, 0x8BU, 0x53U, 0xDEU, 0x84U,
            0xF0U, 0xA1U, 0x03U, 0xAAU, 0x17U, 0x6FU, 0xD3U, 0x10U,
            0x57U, 0x25U, 0xC6U, 0xA3U, 0x8FU, 0x1AU, 0xD3U, 0xD7U,
            0x0DU, 0x89U, 0x22U, 0x3CU, 0x17U, 0xC6U, 0xF7U, 0x02U,
            0x0CU, 0xD9U, 0x0FU, 0xABU, 0x27U, 0xA6U, 0xE3U, 0x20U,
            0xF4U, 0xB2U, 0x21U, 0x12U, 0x85U, 0xF5U, 0x98U, 0x45U,
            0x16U, 0x24U, 0xF7U, 0x46U, 0x5DU, 0x1FU, 0x41U, 0x9AU,
            0x0EU, 0x25U, 0x4AU, 0x99U, 0x27U, 0x5CU, 0x0BU, 0xC8U,
            0x33U, 0x99U, 0x65U, 0x3CU, 0xC9U, 0xDBU, 0x78U, 0x6DU,
            0x36U, 0xC6U, 0x37U, 0x35U, 0x2EU, 0x97U, 0x04U, 0x03U,
            0x26U, 0x78U, 0x5AU, 0x5AU, 0xD8U, 0xEAU, 0xF7U, 0xF1U,
            0xF9U, 0x22U, 0x9DU, 0xDEU, 0xB7U, 0x50U, 0xFEU, 0x91U,
            0xD4U, 0x5EU, 0xA1U, 0xC2U, 0x1BU, 0xE4U, 0x3FU, 0x19U,
            0x50U, 0x21U, 0x1FU, 0x38U, 0xA1U, 0x11U, 0xB1U, 0x08U,
            0x7DU, 0xA2U, 0xBBU, 0xE5U, 0xECU, 0x1BU, 0x9FU, 0x51U,
            0x77U, 0x6CU, 0xB9U, 0xA8U, 0xC2U, 0x76U, 0xD4U, 0xCBU,
            0x11U, 0x7DU, 0x4AU, 0x92U, 0xE2U, 0x0AU, 0x96U, 0x9AU,
            0x28U, 0x72U, 0xFFU, 0xAAU, 0xF8U, 0xD5U, 0x64U, 0x8AU,
            0x45U, 0x71U, 0xF3U, 0xB5U, 0xD7U, 0x2AU, 0x3BU, 0x7FU,
            0x5CU, 0xFCU, 0x2DU, 0x89U, 0xBCU, 0x64U, 0x2FU, 0x63U,
            0x87U, 0xE6U, 0x57U, 0xECU, 0x06U, 0xE3U, 0xDDU, 0x15U,
            0xB4U, 0x27U, 0xA8U, 0x6CU, 0x37U, 0x54U, 0xD3U, 0x37U,
            0xCFU, 0x4DU, 0x71U, 0x04U, 0x91U, 0x08U, 0x1AU, 0xC6U,
            0xBDU, 0xEBU, 0x86U, 0xC5U, 0x5AU, 0x63U, 0x19U, 0xD5U,
            0xC5U, 0xCBU, 0x82U, 0xC3U, 0x54U, 0x57U, 0xD8U, 0x6CU,
            0x8FU, 0x9AU, 0xF1U, 0x7BU, 0x08U, 0x61U, 0xFCU, 0x96U,
            0x1AU, 0xCEU, 0x8FU, 0x11U, 0xE2U, 0xDDU, 0x96U, 0xA3U,
            0x57U, 0xD7U, 0x0DU, 0x28U, 0x06U, 0x0AU, 0xC5U, 0x1FU,
            0xBBU, 0xC6U, 0x67U, 0xC8U, 0xB0U, 0xA7U, 0xDAU, 0x00U,
            0xC3U, 0x00U, 0x21U, 0xACU, 0xFFU, 0xE9U, 0x4FU, 0xB7U,
            0x9AU, 0xC9U, 0x77U, 0xC3U, 0x96U, 0x6EU, 0x1CU, 0xC4U,
            0x61U, 0xAEU, 0x6FU, 0x55U, 0x0CU, 0xDAU, 0x68U, 0x48U,
            0x97U, 0xEDU, 0x9CU, 0x90U, 0x17U, 0x2AU, 0x2DU, 0xC9U,
            0x2AU, 0x77U, 0x87U, 0xECU, 0x64U, 0xF5U, 0x78U, 0x99U,
            0xA9U, 0xB9U, 0x11U, 0x05U, 0xE9U, 0x7BU, 0x3BU, 0x49U,
            0xAEU, 0x61U, 0x70U, 0x0BU, 0xBFU, 0xB7U, 0x67U, 0xA8U,
            0x9FU, 0x02U, 0x30U, 0xD3U, 0x0BU, 0x14U, 0xF0U, 0x89U,
            0x95U, 0x87U, 0x5BU, 0x04U, 0xF3U, 0x27U, 0xEFU, 0x91U,
            0x4AU, 0xD4U, 0x7FU, 0x9EU, 0x73U, 0x95U, 0xE5U, 0x48U,
            0x7FU, 0xE4U, 0xF6U, 0x92U, 0x85U, 0x47U, 0xA5U, 0xFEU,
            0x12U, 0xD7U, 0xC4U, 0x9EU, 0xCFU, 0x8AU, 0xC2U, 0xD2U,
            0x10U, 0x94U, 0xE5U, 0x58U, 0xBBU, 0xE0U, 0xEEU, 0xD9U,
            0x6AU, 0x59U, 0xD9U, 0xBCU, 0xD5U, 0xCDU, 0xA0U, 0xBCU,
            0xD7U, 0x96U, 0xAAU, 0x23U, 0xA9U, 0x10U, 0x6CU, 0x7EU,


            )
        val resultWithXor = Argon2Utils.compressionFunctionG(randomBlock, randomBlock2, randomBlock3, false)
        assertTrue {
            expectedWithXor.contentEquals(resultWithXor)
        }
    }


}