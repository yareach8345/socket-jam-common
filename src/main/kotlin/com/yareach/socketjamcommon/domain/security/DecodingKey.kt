package com.yareach.socketjamcommon.domain.security

import io.jsonwebtoken.JwtParserBuilder
import java.security.PublicKey
import javax.crypto.SecretKey

sealed interface DecodingKey {
    fun verify(jwtParserBuilder: JwtParserBuilder): JwtParserBuilder
}

data class UsePublicKey(private val publicKey: PublicKey) : DecodingKey {
    override fun verify(jwtParserBuilder: JwtParserBuilder): JwtParserBuilder =
        jwtParserBuilder.verifyWith(publicKey)
}

data class UseSecretKey(private val secretKey: SecretKey) : DecodingKey {
    override fun verify(jwtParserBuilder: JwtParserBuilder): JwtParserBuilder =
        jwtParserBuilder.verifyWith(secretKey)
}