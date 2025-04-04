package feature.home.domain.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.eygraber.uri.UriCodec
import core.domain.model.Expense
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ExpenseListNavType : NavType<List<Expense>>(true) {

    override fun get(bundle: Bundle, key: String): List<Expense>? {
        return Json.decodeFromString(bundle.getString(key) ?: return emptyList())
    }

    override fun parseValue(value: String): List<Expense> {
        return Json.decodeFromString(UriCodec.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: List<Expense>) {
        bundle.putString(key , Json.encodeToString(value))
    }

    override fun serializeAsValue(value: List<Expense>): String {
        return UriCodec.encode(Json.encodeToString(value))
    }
}