package com.yareach.socketjamcommon.util

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object JwtUtil {
    fun stringToPublicKey(publicKeyString: String): PublicKey {
        val encoded: ByteArray = Base64.getDecoder().decode(publicKeyString)

        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = X509EncodedKeySpec(encoded)
        return keyFactory.generatePublic(keySpec)
    }

    fun stringToPrivateKey(privateKeyString: String): PrivateKey {
        val encoded: ByteArray = Base64.getDecoder().decode(privateKeyString)

        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = PKCS8EncodedKeySpec(encoded)
        return keyFactory.generatePrivate(keySpec)
    }
}