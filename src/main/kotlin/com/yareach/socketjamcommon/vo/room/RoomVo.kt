package com.yareach.socketjamcommon.vo.room

data class RoomVo(
    val id: String,
    val roomName: String,
    val users: List<RoomMemberVo>,
    val userCount: Int = users.size,
    val maxUserCount: Int
)