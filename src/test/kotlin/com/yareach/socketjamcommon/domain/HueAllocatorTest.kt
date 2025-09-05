package com.yareach.socketjamcommon.domain

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HueAllocatorTest {
    @Test
    @DisplayName("HueAllocator로 아직 배정되지 않은 색깔을 가져올 수 있음")
    fun getHueValueWithHueAllocator() {
        val maxUserCount = 10
        val usedHueValues = (0 until 5).map { it * 36 }.toSet()
        val hueAllocator = HueAllocator(usedHueValues, maxUserCount)

        val newHueValue = hueAllocator.getRandomHueValue()

        assertTrue(usedHueValues.none{ it == newHueValue })
        assertEquals(0, newHueValue % (360 / maxUserCount))
    }

    @Test
    @DisplayName("HueAllocator로 받는 값은 아직 배정되지 않은 색깔이어야 한다")
    fun gottenHueValueIsNotAUsedValue() {
        val maxUserCount = 10
        val availableHueValues = (0 until 10).map { it * 36 }.toSet()

        val randomHueValue = availableHueValues.random()

        val usedHueValues = availableHueValues.filter { it != randomHueValue }.toSet()

        val hueAllocator = HueAllocator(
            usedHueValues,
            maxUserCount
        )

        val newHueValue = hueAllocator.getRandomHueValue()

        assertTrue(usedHueValues.none{ it == newHueValue })
        assertEquals(randomHueValue, newHueValue)
    }

    @Test
    @DisplayName("usedHueValues가 얻을 수 있는 모든 값을 다 가지고 있으면(더 이상 배정할 색이 없으면) 실패한다")
    fun canInitializeHueAllocatorWithASetHaveAllHueValues() {
        val maxUserCount = 10
        val usedHueValues = (0 until 10).map { it * 36 }.toSet()

        val exception = assertThrows<IllegalArgumentException> { HueAllocator(usedHueValues, maxUserCount) }
        assertEquals("Count of values in usedHueValues(10) set is less than maxUserCount(10)", exception.message)
    }

    @Test
    @DisplayName("usedHueValues에 얻을 수 없는 값이 있다면 실패한다")
    fun canInitializeHueAllocatorWithASetHaveIllegalHueValue() {
        val maxUserCount = 10
        val usedHueValues = setOf(36, 72, 1)

        val exception = assertThrows<IllegalArgumentException> { HueAllocator(usedHueValues, maxUserCount) }
        assertEquals("Values in usedHueValues set must divisible by 360 / maxUserCount(${360 / maxUserCount})", exception.message)
    }

    @Test
    @DisplayName("usedHueValues에 0..<360 범위를 초과하는 값이 있다면 실패")
    fun canInitializeHueAllocatorWithASetHaveOutlierValue() {
        val maxUserCount = 10
        val usedHueValues = setOf(36, 72, 396)

        val exception = assertThrows<IllegalArgumentException> { HueAllocator(usedHueValues, maxUserCount) }
        assertEquals("Values in usedHueValues set must be in 0..<360", exception.message)
        assertTrue(usedHueValues.all{ it % 36 == 0 })
    }
}