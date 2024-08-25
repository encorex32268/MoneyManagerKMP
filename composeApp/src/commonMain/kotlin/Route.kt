import feature.core.domain.model.Expense
import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data class  HomeAdd(
    val expense: Expense,
    val isAddNew: Boolean
)

@Serializable
data class  HomeEdit(
    val expense: Expense
)

@Serializable
data object Chart

@Serializable
data class  ChartDetail(
    val items: List<Expense>
)

