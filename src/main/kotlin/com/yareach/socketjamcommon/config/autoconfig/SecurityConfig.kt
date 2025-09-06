package com.yareach.socketjamcommon.config.autoconfig

import com.yareach.socketjamcommon.config.security.JwtAuthenticationConverter
import com.yareach.socketjamcommon.util.JwtUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@AutoConfiguration
@ComponentScan("com.yareach.socketjamcommon")
class SecurityConfig {

    @Bean
    fun jwtUtils(
        @Value("\${spring.jwt.secret}") secretSecret: String,
        @Value("\${spring.jwt.expiration:86400000}") expiredMs: Long = 24 * 60 * 60 * 1000,
    ): JwtUtils = JwtUtils(secretSecret, expiredMs)

    @Bean
    fun jwtAuthenticationConverter(
        jwtUtils: JwtUtils
    ) = JwtAuthenticationConverter(jwtUtils)
}