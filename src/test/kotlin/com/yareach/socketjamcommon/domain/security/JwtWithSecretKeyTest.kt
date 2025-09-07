package com.yareach.socketjamcommon.domain.security

import com.yareach.socketjamcommon.util.JwtUtil
import com.yareach.socketjamcommon.vo.user.UserVo
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.Date
import java.util.UUID
import javax.crypto.spec.SecretKeySpec
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class JwtWithSecretKeyTest {
    private val jwtUtil = JwtUtil()

    private val testSecretKeyString: String = "a-string-secret-256-bits-long-for-test"

    private val secretKey = jwtUtil.stringToSecretKey(testSecretKeyString)

    private val jwtEncoder = JwtTokenEncoder.fromSecretKey(secretKey)
    private val jwtDecoder = JwtTokenDecoder.fromSecretKey(secretKey)

    val testUser = UserVo(UUID.randomUUID(), "testUser")

    @Test
    @DisplayName("Token 생성 테스트")
    fun generateToken() {
        val jwt = jwtEncoder.createJwt(testUser.nickName, testUser.userId)

        assertTrue(jwtDecoder.isValidToken(jwt))

        assertEquals(testUser.nickName, jwtDecoder.getNickName(jwt))
        assertEquals(testUser.userId.toString(), jwtDecoder.getUserId(jwt))

        val userVo = jwtDecoder.getUserVo(jwt)

        assertEquals(testUser.nickName, userVo.nickName)
        assertEquals(testUser.userId, userVo.userId)
    }


    @Test
    @DisplayName("UserVo로 token 생성")
    fun generateTokenWithUserVo() {
        val jwt = jwtEncoder.createJwt(testUser)

        assertTrue(jwtDecoder.isValidToken(jwt))

        assertEquals(testUser.nickName, jwtDecoder.getNickName(jwt))
        assertEquals(testUser.userId.toString(), jwtDecoder.getUserId(jwt))

        val userVo = jwtDecoder.getUserVo(jwt)

        assertEquals(testUser.nickName, userVo.nickName)
        assertEquals(testUser.userId, userVo.userId)
    }

    @Test
    @DisplayName("올바른 secret key로만 파싱 가능")
    fun parsingTokenWithRightSecretKeyType() {
        val secretKey = SecretKeySpec(
            testSecretKeyString.toByteArray(),
            Jwts.SIG.HS256.key().build().algorithm
        )

        val token = jwtEncoder.createJwt(testUser)

        assertTrue { Jwts.parser().verifyWith(secretKey).build().isSigned(token) }
        assertEquals(
            testUser.nickName,
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload["nickName"]
        )
        assertEquals(
            testUser.userId.toString(),
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload["userId"]
        )
    }

    @Test
    @DisplayName("잘못된 secret key를 사용하면 실패")
    fun parsingTokenWithWrongSecretKey() {
        val wrongSecretString = "a-wrong-key-256-bits-long-for-test"
        val wrongSecretKey = SecretKeySpec(
            wrongSecretString.toByteArray(),
            Jwts.SIG.HS256.key().build().algorithm
        )

        val token = jwtEncoder.createJwt(testUser)

        assertFailsWith(SignatureException::class) {
            Jwts.parser().verifyWith(wrongSecretKey).build().parseSignedClaims(token)
        }
    }

    @Test
    @DisplayName("payload의 value값이 string이 아니면 실패")
    fun parsingTokenWithWrongPayloadValueType() {
        val secretKey = SecretKeySpec(
            testSecretKeyString.toByteArray(),
            Jwts.SIG.HS256.key().build().algorithm
        )

        val token = Jwts.builder()
            .claim("userName", 12345) // string이 아닌 Int 사용
            .claim("userId", 12345) // string이 아닌 Int 사용
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + 1000 * 60))
            .signWith(secretKey)
            .compact()

        assertFailsWith(IllegalArgumentException::class) { jwtDecoder.getNickName(token) }
        assertFailsWith(IllegalArgumentException::class) { jwtDecoder.getUserId(token) }
    }
}