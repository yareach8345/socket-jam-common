package com.yareach.socketjamcommon.enums

import org.junit.jupiter.api.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals

class ServiceHostTest {
    @Test
    @DisplayName("ServiceHost로 호스트의 uri를 얻음")
    fun getServiceHostTest() {
        val auth = ServiceHost.AUTH

        assertEquals("http://socket-jam-auth.com", auth.toString())
        assertEquals("http://socket-jam-auth.com", auth.uri)
    }

    @Test
    @DisplayName("withPath 메서드로 path를 포함한 uri를 얻음")
    fun getServiceHostWithPathTest() {
        val auth = ServiceHost.AUTH
        assertEquals("http://socket-jam-auth.com/api/v1", auth.withPath("/api/v1"))
    }
}