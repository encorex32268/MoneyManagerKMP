package feature.core.domain

interface KeySettings {

    companion object{
        const val IS_SET_DEFAULT_TYPES = "default_types"
    }

    fun setIsSetDefaultTypes(isSet: Boolean)
    fun getIsSetDefaultTypes(): Boolean
}