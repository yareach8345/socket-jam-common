package com.yareach.socketjamcommon.service.enum

enum class ServiceHost(val uri: String) {
    AUTH("http://socket-jam-auth"),
    ROOM("http://socket-jam-backend"),
    SOCKET("http://socket-jam-socket");

    override fun toString() = uri

    /**
     * /path 형태의 경로를 받아 http://host/path 형태로 반환
     *
     * path는 /(루트)로 시작해야 함
     *
     * 예시)
     *   /api/v1/test   옳은 형식
     *   api/v1/test    잘못된 형식
     */
    fun withPath(path: String) = "$uri$path"
}