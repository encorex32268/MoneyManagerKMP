import java.text.NumberFormat
import java.util.Locale

actual fun Double.format(digits: Int): String {
    return "%.${digits}f".format(this)
}

actual fun Long.toMoneyString(): String {
    return NumberFormat.getCurrencyInstance(
        Locale.getDefault()
    ).apply {
        maximumFractionDigits = 0
    }.format(this)
}

actual fun toDateString(month: Int, day: Int): String {
    return String.format(locale = Locale.getDefault(),"%02d/%02d",month,day)
}