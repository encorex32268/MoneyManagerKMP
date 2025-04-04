import java.util.UUID

actual class UUID{

    val uuid = UUID.randomUUID()

    actual fun generateUUID(): String {
        return uuid.toString()
    }

    actual fun generateUUIDInt(): Int {
        return uuid.leastSignificantBits.toInt()
    }
}