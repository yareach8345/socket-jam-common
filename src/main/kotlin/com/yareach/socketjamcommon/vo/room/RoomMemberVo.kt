package com.yareach.socketjamcommon.vo.room

import java.time.LocalDateTime
import java.util.UUID

data class RoomMemberVo(
    val userId: UUID,
    val colorHue: Int,
    val nickName: String,
    val joinedAt: LocalDateTime
)