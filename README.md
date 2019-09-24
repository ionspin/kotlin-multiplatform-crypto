[![Build Status](https://travis-ci.com/ionspin/kotlin-multiplatform-crypto.svg?branch=master)](https://travis-ci.com/ionspin/kotlin-multiplatform-crypto)
![Maven Central](https://img.shields.io/maven-central/v/com.ionspin.kotlin/multiplatform-crypto.svg)

# Kotlin Multiplatform Crypto Library

Kotlin Multiplatform Crypto is a library for various cryptographic applications. 

This is an extremely early release, currently only consisting of Blake2b and SHA256 and 512.

API is very opinionated, ment to be used on both encrypting and decrypting side. The idea is that API leaves less room for 
errors when using it.

## Notes & Roadmap

**The API will move fast and break often until v1.0**

Make SHA hashes "updatable" like Blake2b

After that tenative plan is to add 25519 curve based signing and key exchange next.

## Should I use this in production?

No.

## Should I use this in code that is critical in any way, shape or form?

No.

## Why?

This is an experimental implementation, mostly for expanding personal understanding of cryptography. 
It's not peer reviewed, not guaranteed to be bug free, and not guaranteed to be secure.

## Integration



## Hashing functions
* Blake2b
* SHA512
* SHA256

## Symmetric cipher (Currently only available only in 0.0.3-SNAPSHOT)
* AES
  * Modes: CBC, CTR

More to come.

## Integration

#### Gradle
```kotlin
implementation("com.ionspin.kotlin:crypto:0.0.2")
```

#### Snapshot builds
```kotlin
repositories {
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}
implementation("com.ionspin.kotlin:crypto:0.0.3-SNAPSHOT")

```

## Usage

### Hashes

Hashes are provided in two versions, "stateless", usually the companion object of the hash, 
which takes the data to be hashed in one go, and "updatable" which can be fed data in chunks.


#### Blake2b

You can use Blake 2b in two modes

##### Stateless version
You need to deliver the complete data that is to be hashed in one go

```kotlin
val input = "abc"
val result = Blake2b.digest(input)
```

Result is returned as a `Array<Byte>`

##### Updatable instance version
You can create an instance and feed the data by using `update(input : Array<Byte>)` call. Once all data is supplied,
you should call `digest()` or `digestString()` convenience method that converts the `Array<Byte>` into hexadecimal string.

If you want to use Blake2b with a key, you should supply it when creating the `Blake2b` instance.

```kotlin
val test = "abc"
val key = "key"
val blake2b = Blake2b(key)
blake2b.update(test)
val result = blake2b.digest()
```

After digest is called, the instance is reset and can be reused (Keep in mind key stays the same for the particular instance).
#### SHA2 (SHA256 and SHA512)

##### Stateless version

You need to deliver the complete data that is to be hashed in one go. You can either provide the `Array<Byte>` as input
or `String`. Result is always returned as `Array<Byte>` (At least in verision 0.0.1)

```kotlin
val input = "abc"
val result = Sha256.digest(input)
```

```kotlin
val input ="abc"
val result = Sha512.digest(message = input.encodeToByteArray().map { it.toUByte() }.toTypedArray())
```

Result is returned as a `Array<Byte>`

##### Updateable version

Or you can use the updatable instance version

```kotlin
val sha256 = Sha256()
sha256.update("abc")
val result = sha256.digest()
```

```kotlin
val sha512 = Sha512()
sha512.update("abc")
val result = sha512.digest()
```
### Symmetric encryption

#### AES

Aes is available with CBC and CTR mode through `AesCbc` and `AesCtr` classes/objects. 
Similarly to hashes you can either use stateless or updateable version.

Initialization vector, or counter states are chosen by the SDK automaticaly, and returned alongside encrypted data

##### Stateless AesCbc and AesCtr 

AesCtr

```kotlin
val keyString = "4278b840fb44aaa757c1bf04acbe1a3e"
val key = AesKey.Aes128Key(keyString)
val plainText = "6bc1bee22e409f96e93d7e117393172aae2d8a571e03ac9c9eb76fac45af8e5130c81c46a35ce411e5fbc1191a0a52eff69f2445df4f9b17ad2b417be66c3710"

val encryptedDataAndInitializationVector = AesCtr.encrypt(key, plainText.hexStringToUByteArray())
val decrypted = AesCtr.decrypt(
    key,
    encryptedDataAndInitializationVector.encryptedData,
    encryptedDataAndInitializationVector.initialCounter
)
plainText == decrypted.toHexString()
```

AesCbc

```kotlin

val keyString = "4278b840fb44aaa757c1bf04acbe1a3e"
val key = AesKey.Aes128Key(keyString)

val plainText = "3c888bbbb1a8eb9f3e9b87acaad986c466e2f7071c83083b8a557971918850e5"

val encryptedDataAndInitializationVector = AesCbc.encrypt(key, plainText.hexStringToUByteArray())
val decrypted = AesCbc.decrypt(
    key,
    encryptedDataAndInitializationVector.encryptedData,
    encryptedDataAndInitializationVector.initilizationVector
)
plainText == decrypted.toHexString()

```
















 