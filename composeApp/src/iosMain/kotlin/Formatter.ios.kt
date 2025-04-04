import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSString
import platform.Foundation.currentLocale
import platform.Foundation.stringWithFormat

actual fun Double.format(digits: Int): String {
    return NSString.stringWithFormat("%.${digits}f", this) as String
}

actual fun Long.toMoneyString(): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        locale = NSLocale.currentLocale
        maximumFractionDigits = 0U
    }
    return formatter.stringFromNumber(
        number = NSNumber(long = this)) ?: ""
}
