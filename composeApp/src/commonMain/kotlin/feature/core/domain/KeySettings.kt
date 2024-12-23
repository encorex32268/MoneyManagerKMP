package feature.core.domain

interface KeySettings {

    companion object{
        const val IS_SET_DEFAULT_TYPES = "default_types"
        const val CLOSE_AD_BANNER = "close_ad_banner"
        const val IS_DARK_THEME = "darkLightMode"
    }

    fun setIsSetDefaultTypes(isSet: Boolean)
    fun getIsSetDefaultTypes(): Boolean

    fun setCloseAdBanner(isClosed: Boolean)
    fun getCloseAdBanner(): Boolean

    fun setDarkLightMode(isDark: Boolean)
    fun getDarkLightMode(): Boolean
}