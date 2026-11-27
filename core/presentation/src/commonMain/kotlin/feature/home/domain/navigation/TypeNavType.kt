package feature.home.domain.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.eygraber.uri.UriCodec
import core.domain.model.Type
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object TypeNavType : NavType<Type>(true) {

    override fun get(bundle: SavedState, key: String): Type? {
        val raw = bundle.read { getStringOrNull(key) } ?: return null
        return Json.decodeFromString(raw)
    }

    override fun parseValue(value: String): Type {
        return Json.decodeFromString(UriCodec.decode(value))
    }

    override fun put(bundle: SavedState, key: String, value: Type) {
        bundle.write { putString(key, Json.encodeToString(value)) }
    }

    override fun serializeAsValue(value: Type): String {
        return UriCodec.encode(Json.encodeToString(value))
    }
}