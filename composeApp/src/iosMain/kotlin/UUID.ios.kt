@file:OptIn(ExperimentalForeignApi::class)

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreFoundation.CFUUIDCreate
import platform.CoreFoundation.kCFAllocatorDefault

actual class UUID {

    val uuid = CFUUIDCreate(kCFAllocatorDefault)

    actual fun generateUUID(): String {
        return uuid.toString()
    }

    actual fun generateUUIDInt(): Int {
       return uuid.hashCode()
    }
}