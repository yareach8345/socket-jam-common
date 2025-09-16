package com.yareach.socketjamcommon.dto.room

import com.yareach.socketjamcommon.dto.user.RoomMemberDto

data class RoomDto(
    val id: String,
    val roomName: String,
    val users: List<RoomMemberDto>,
    val userCount: Int = users.size,
    val maxUserCount: Int
)