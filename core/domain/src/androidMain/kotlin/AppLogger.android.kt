import android.util.Log

actual object AppLogger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        runCatching {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }.onFailure {
            println("E/$tag: $message ${throwable?.message.orEmpty()}")
        }
    }

    actual fun d(tag: String, message: String) {
        runCatching {
            Log.d(tag, message)
        }.onFailure {
            println("D/$tag: $message")
        }
    }

    actual fun i(tag: String, message: String) {
        runCatching {
            Log.i(tag, message)
        }.onFailure {
            println("I/$tag: $message")
        }
    }
}
