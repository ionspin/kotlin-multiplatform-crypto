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

import com.ionspin.kotlin.crypto.util.hexStringToUByteArray
import com.ionspin.kotlin.crypto.util.toHexString
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Created by Ugljesa Jovanovic
 * ugljesa.jovanovic@ionspin.com
 * on 21-Sep-2019
 */

class AesCbcTest {

    @Test
    fun testCbcEncryption() {
        assertTrue {
            val key = "4278b840fb44aaa757c1bf04acbe1a3e"
            val iv = "57f02a5c5339daeb0a2908a06ac6393f"
            val plaintext = "3c888bbbb1a8eb9f3e9b87acaad986c466e2f7071c83083b8a557971918850e5"
            val expectedCipherText = "479c89ec14bc98994e62b2c705b5014e" +
                    "175bd7832e7e60a1e92aac568a861eb7" +
                    "fc2dc2f4a527ce39f79c56b31432c779"
            val aesCbc =
                AesCbcPure(InternalAesKey.Aes128Key(key), mode = Mode.ENCRYPT, initializationVector = iv.hexStringToUByteArray())
            aesCbc.addData(plaintext.hexStringToUByteArray())
            val encrypted = aesCbc.encrypt()
            println("Encrypted: ${encrypted.encryptedData.toHexString()}")

            expectedCipherText == encrypted.encryptedData.toHexString() &&
                    iv == encrypted.initializationVector.toHexString()
        }



    }

    @Test
    fun testEncryptionApi() {
        assertTrue {
            val keyString = "4278b840fb44aaa757c1bf04acbe1a3e"
            val key = InternalAesKey.Aes128Key(keyString)

            val plainText = "3c888bbbb1a8eb9f3e9b87acaad986c466e2f7071c83083b8a557971918850e5"

            val encryptedDataAndInitializationVector = AesCbcPure.encrypt(key, plainText.hexStringToUByteArray())
            val decrypted = AesCbcPure.decrypt(
                key,
                encryptedDataAndInitializationVector.encryptedData,
                encryptedDataAndInitializationVector.initializationVector
            )
            plainText == decrypted.toHexString()
        }
    }

    @Test
    fun testCbcDecryption() {
        assertTrue {
            val key = "4278b840fb44aaa757c1bf04acbe1a3e"
            val iv = "57f02a5c5339daeb0a2908a06ac6393f"
            val cipherText = "479c89ec14bc98994e62b2c705b5014e" +
                    "175bd7832e7e60a1e92aac568a861eb7" +
                    "fc2dc2f4a527ce39f79c56b31432c779"
            val expectedPlainText = "3c888bbbb1a8eb9f3e9b87acaad986c466e2f7071c83083b8a557971918850e5"
            val aesCbc =
                AesCbcPure(InternalAesKey.Aes128Key(key), mode = Mode.DECRYPT, initializationVector = iv.hexStringToUByteArray())
            aesCbc.addData(cipherText.hexStringToUByteArray())
            val decrypted = aesCbc.decrypt()
            println("Decrypted: ${decrypted.toHexString()}")

            expectedPlainText == decrypted.toHexString()
        }



    }

    @Test
    fun testDecryptionApi() {
        assertTrue {
            val key = "4278b840fb44aaa757c1bf04acbe1a3e"
            val iv = "57f02a5c5339daeb0a2908a06ac6393f"
            val cipherText = "479c89ec14bc98994e62b2c705b5014e" +
                    "175bd7832e7e60a1e92aac568a861eb7" +
                    "fc2dc2f4a527ce39f79c56b31432c779"
            val expectedPlainText = "3c888bbbb1a8eb9f3e9b87acaad986c466e2f7071c83083b8a557971918850e5"
            val decrypted = AesCbcPure.decrypt(InternalAesKey.Aes128Key(key), cipherText.hexStringToUByteArray(), iv.hexStringToUByteArray())
            println("Decrypted: ${decrypted.toHexString()}")

            expectedPlainText == decrypted.toHexString()
        }
    }



}
