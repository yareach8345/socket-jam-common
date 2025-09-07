package com.yareach.socketjamcommon.config.security

import com.yareach.socketjamcommon.domain.security.JwtTokenDecoder
import com.yareach.socketjamcommon.domain.security.JwtTokenEncoder
import com.yareach.socketjamcommon.vo.user.UserVo
import org.junit.jupiter.api.DisplayName
import java.util.UUID
import kotlin.test.*

class JwtAuthenticationConverterTest {
    val testSecretKey = "a-string-secret-256-bits-long-for-test"
    val jwtDecoder = JwtTokenDecoder.fromSecretKey(testSecretKey)
    val jwtEncoder = JwtTokenEncoder.fromSecretKey(testSecretKey)
    val converter = JwtAuthenticationConverter(jwtDecoder)

    val testUser = UserVo(UUID.randomUUID(), "testuser1234")

    val testToken = jwtEncoder.createJwt(testUser)

    @Test
    @DisplayName("AuthHeader를 UsernamePasswordToken으로 변환")
    fun convertAuthHeader() {
        val testAuthHeader = "Bearer $testToken"
        val result = converter.convert(testAuthHeader)

        val userDetail = result?.principal as CustomUserDetail

        assertNotNull(result)
        assertEquals(userDetail.nickName, testUser.nickName)
        assertEquals(userDetail.userId, testUser.userId.toString())
    }

    @Test
    @DisplayName("'Bearer '로 시작하지 않는 헤더의 경우 convert 실패")
    fun failConvertAuthHeaderWithHeaderNotStartsWithBearer() {
        val testAuthHeaderNotStartsWithBearer = testToken

        val result = converter.convert(testAuthHeaderNotStartsWithBearer)

        assertNull(result)
    }

    @Test
    @DisplayName("'Bearer '로 시작하더라도 유효하지 않는 토큰일 경우 convert 실패")
    fun failConvertAuthHeaderWithIllegalHeader() {
        //an wrong authentication header
        val testWrongHeader = "Bearer 1234567890qwertyuiopasdfghjklzxcvbnm"

        val result = converter.convert(testWrongHeader)

        assertNull(result)
    }
}