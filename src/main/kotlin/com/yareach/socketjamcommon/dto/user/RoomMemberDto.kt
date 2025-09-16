package com.yareach.socketjamcommon.dto.user

import java.time.LocalDateTime
import java.util.UUID

data class RoomMemberDto(
    val userId: UUID,
    val colorHue: Int,
    val nickName: String,
    val joinedAt: LocalDateTime
)