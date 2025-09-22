package com.yareach.socketjamcommon.resource.utils

import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class ResourceUtil {
    fun readResourceFile(path: String): String {
        val resource = ClassPathResource(path)
        return resource.inputStream.bufferedReader().use { it.readText() }
    }
}