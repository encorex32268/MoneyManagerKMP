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
}