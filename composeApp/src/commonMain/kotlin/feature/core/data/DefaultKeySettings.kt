package feature.core.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import feature.core.domain.KeySettings

class DefaultKeySettings(
    private val settings: Settings
): KeySettings {
    override fun setIsSetDefaultTypes(isSet: Boolean) {
        settings.set(
            key = KeySettings.IS_SET_DEFAULT_TYPES,
            value = isSet
        )
    }

    override fun getIsSetDefaultTypes(): Boolean {
        return settings.get(
            key = KeySettings.IS_SET_DEFAULT_TYPES,
            defaultValue = false
        )
    }

    override fun setCloseAdBanner(isClosed: Boolean) {
        settings.set(
            key = KeySettings.CLOSE_AD_BANNER,
            value = isClosed
        )
    }

    override fun getCloseAdBanner(): Boolean {
        return settings.get(
            key = KeySettings.CLOSE_AD_BANNER,
            defaultValue = false
        )
    }

    override fun setDarkLightMode(isDark: Boolean) {
        settings.set(
            key = KeySettings.IS_DARK_THEME,
            value = isDark
        )
    }

    override fun getDarkLightMode(): Boolean {
        return settings.get(
            key = KeySettings.IS_DARK_THEME,
            defaultValue = false
        )
    }
}