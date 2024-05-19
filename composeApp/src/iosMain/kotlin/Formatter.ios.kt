import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSString
import platform.Foundation.currentLocale
import platform.Foundation.stringWithFormat

actual fun formatString(
    format: String, vararg args: Any
): String {
    return NSString.stringWithFormat(format,args)
}

actual fun Long.toMoneyString(): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        locale = NSLocale.currentLocale
    }
    return formatter.stringFromNumber(NSNumber(this)) ?: ""
}