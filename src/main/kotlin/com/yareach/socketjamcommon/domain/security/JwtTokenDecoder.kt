package com.yareach.socketjamcommon.domain.security

import com.yareach.socketjamcommon.vo.user.UserVo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.security.KeyFactory
import java.util.UUID
import javax.crypto.spec.SecretKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64


class JwtTokenDecoder(
    private val decodingKey: DecodingKey,
) {
    companion object {
        fun fromSecretKey(secretKeyString: String): JwtTokenDecoder {
            val secretKey = SecretKeySpec(
                secretKeyString.toByteArray(),
                Jwts.SIG.HS256.key().build().algorithm
            )

            return JwtTokenDecoder(UseSecretKey(secretKey))
        }

        fun fromPublicKey(publicKeyString: String): JwtTokenDecoder {
            val encoded: ByteArray = Base64.getDecoder().decode(publicKeyString)

            val keyFactory = KeyFactory.getInstance("RSA")
            val keySpec = X509EncodedKeySpec(encoded)
            val publicKey = keyFactory.generatePublic(keySpec)

            return JwtTokenDecoder(UsePublicKey(publicKey))
        }
    }

    fun getPayload(token: String): Claims =
        Jwts
            .parser()
            .let(decodingKey::verify)
            .build()
            .parseSignedClaims(token)
            .payload

    fun getNickName(token: String) = getPayload(token)["nickName"]
        .takeIf { it is String } as String? ?: throw kotlin.IllegalArgumentException("userName is must be String")

    fun getUserId(token: String) = getPayload(token)["userId"]
        .takeIf { it is String } as String? ?: throw kotlin.IllegalArgumentException("userId is must be String")

    fun isValidToken(token: String): Boolean = Jwts.parser().let(decodingKey::verify).build().isSigned(token)

    fun getUserVo(token: String): UserVo {
        val nickName = getNickName(token)
        val userId = getUserId(token)

        return UserVo(UUID.fromString(userId), nickName)
    }
}