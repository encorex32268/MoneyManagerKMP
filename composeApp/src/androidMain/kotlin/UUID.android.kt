
actual class UUID{

    val uuid = java.util.UUID.randomUUID()

    actual fun generateUUID(): String {
        return uuid.toString()
    }

    actual fun generateUUIDInt(): Int {
        return uuid.leastSignificantBits.toInt()
    }
}