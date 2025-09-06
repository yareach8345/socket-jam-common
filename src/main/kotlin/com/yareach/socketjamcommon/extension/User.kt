package com.yareach.socketjamcommon.extension

import com.yareach.socketjamcommon.config.security.CustomUserDetail
import com.yareach.socketjamcommon.vo.user.UserVo
import java.util.UUID

fun CustomUserDetail.toUserVo() = UserVo(
    UUID.fromString(userId),
    nickName
)