package com.yareach.socketjamcommon.room.dto

import com.yareach.socketjamcommon.user.dto.RoomMemberDto

data class RoomDto(
    val id: String,
    val roomName: String,
    val users: List<RoomMemberDto>,
    val userCount: Int = users.size,
    val maxUserCount: Int
)