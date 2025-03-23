package feature.home.domain.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.eygraber.uri.UriCodec
import core.domain.model.Type
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object TypeNavType : NavType<Type>(true) {

    override fun get(bundle: Bundle, key: String): Type? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): Type {
        return Json.decodeFromString(UriCodec.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: Type) {
        bundle.putString(key , Json.encodeToString(value))
    }

    override fun serializeAsValue(value: Type): String {
        return UriCodec.encode(Json.encodeToString(value))
    }
}