package com.yareach.socketjamcommon.domain.user

import com.yareach.socketjamcommon.vo.room.RoomVo

class HueAllocator(
    private val usedHueValues: Set<Int>,
    maxUserCount: Int,
) {
    private val usableHueValues: List<Int>

    init {
        val gapOfHue = 360 / maxUserCount

        require(
            usedHueValues.all { it % gapOfHue == 0 }
        ) { "Values in usedHueValues set must divisible by 360 / maxUserCount(${gapOfHue})" }
        require(
            usedHueValues.all { it in 0..<360 }
        ) { "Values in usedHueValues set must be in 0..<360" }
        require(
            usedHueValues.size < maxUserCount,
        ) { "Count of values in usedHueValues(${usedHueValues.size}) set is less than maxUserCount(${maxUserCount})" }

        usableHueValues = (0 until maxUserCount)
            .map { it * gapOfHue }
            .filterNot { usedHueValues.contains(it) }
    }

    fun getRandomHueValue() = usableHueValues.random()
}

fun HueAllocator(roomVo: RoomVo): HueAllocator {
    val usedHueValues = roomVo.users.map { it.colorHue }.toSet()
    return HueAllocator(usedHueValues, roomVo.maxUserCount)
}