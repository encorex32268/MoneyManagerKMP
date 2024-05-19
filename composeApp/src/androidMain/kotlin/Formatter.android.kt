import java.text.NumberFormat
import java.util.Locale

actual fun formatString(
    format: String, vararg args: Any
): String {
    return String.format(format , args)
}


actual fun Long.toMoneyString(): String {
    return NumberFormat.getCurrencyInstance(
        Locale.getDefault()
    ).format(this)
}