package com.yareach.socketjamcommon.utils

import com.yareach.socketjamcommon.vo.auth.JwkVo
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class KetConverter {
    fun stringToPublicKey(publicKeyString: String): RSAPublicKey {
        val publicKeyPEM = publicKeyString
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s+".toRegex(), "")

        val encoded: ByteArray = Base64.getDecoder().decode(publicKeyPEM)

        val keySpec = X509EncodedKeySpec(encoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }

    fun rsaPublicKeyToString(publicKey: RSAPublicKey): String {
        val encoded = Base64.getEncoder().encodeToString(publicKey.encoded)
        return buildString {
            appendLine("-----BEGIN PUBLIC KEY-----")
            encoded.chunked(64).forEach { appendLine(it) }
            appendLine("-----END PUBLIC KEY-----")
        }
    }

    fun publicKeyToJwk(publicKey: RSAPublicKey): JwkVo {
        val nBytes = publicKey.modulus.toByteArray()
        val eBytes = publicKey.publicExponent.toByteArray()

        val encoder = Base64.getUrlEncoder().withoutPadding()
        val nBase64 = encoder.encodeToString(nBytes)
        val eBase64 = encoder.encodeToString(eBytes)

        return JwkVo(
            kty = "RSA",
            n = nBase64,
            e = eBase64
        )
    }

    fun jwkToPublicKey(jwk: JwkVo): RSAPublicKey {
        val decoder = Base64.getUrlDecoder()
        val modulus = BigInteger(1, decoder.decode(jwk.n))
        val exponent = BigInteger(1, decoder.decode(jwk.e))

        val spec = RSAPublicKeySpec(modulus, exponent)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(spec) as RSAPublicKey
    }

    fun stringToPrivateKey(privateKeyString: String): RSAPrivateKey {
        val privateKeyPEM = privateKeyString
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s+".toRegex(), "")

        val encoded: ByteArray = Base64.getDecoder().decode(privateKeyPEM)

        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = PKCS8EncodedKeySpec(encoded)
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }

    fun rsaPrivateKeyToString(privateKey: RSAPrivateKey): String {
        val encoded = Base64.getEncoder().encodeToString(privateKey.encoded)
        return buildString {
            appendLine("-----BEGIN PRIVATE KEY-----")
            encoded.chunked(64).forEach { appendLine(it) }
            appendLine("-----END PRIVATE KEY-----")
        }
    }

    fun stringToSecretKey(secretKeyString: String): SecretKey {
        return SecretKeySpec(
            secretKeyString.toByteArray(),
            Jwts.SIG.HS256.key().build().algorithm
        )
    }

    fun secretKeyToString(secretKey: SecretKey): String {
        return String(secretKey.encoded, Charsets.UTF_8).trim()
    }
}