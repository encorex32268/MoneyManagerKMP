package feature.home.domain.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.eygraber.uri.UriCodec
import core.domain.model.Expense
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ExpenseNavType : NavType<Expense>(true) {

    override fun get(bundle: Bundle, key: String): Expense? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): Expense {
        return Json.decodeFromString(UriCodec.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: Expense) {
        bundle.putString(key , Json.encodeToString(value))
    }

    override fun serializeAsValue(value: Expense): String {
        return UriCodec.encode(Json.encodeToString(value))
    }
}