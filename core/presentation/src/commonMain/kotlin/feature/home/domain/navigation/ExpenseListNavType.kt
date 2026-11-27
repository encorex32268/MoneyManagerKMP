package feature.home.domain.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.eygraber.uri.UriCodec
import core.domain.model.Expense
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ExpenseListNavType : NavType<List<Expense>>(true) {

    override fun get(bundle: SavedState, key: String): List<Expense>? {
        val raw = bundle.read { getStringOrNull(key) } ?: return emptyList()
        return Json.decodeFromString(raw)
    }

    override fun parseValue(value: String): List<Expense> {
        return Json.decodeFromString(UriCodec.decode(value))
    }

    override fun put(bundle: SavedState, key: String, value: List<Expense>) {
        bundle.write { putString(key, Json.encodeToString(value)) }
    }

    override fun serializeAsValue(value: List<Expense>): String {
        return UriCodec.encode(Json.encodeToString(value))
    }
}