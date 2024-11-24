import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.core.domain.KeySettings
import feature.core.domain.model.Type
import feature.core.domain.repository.TypeRepository
import feature.core.presentation.CategoryList
import io.realm.kotlin.Realm
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.launch

class AppViewModel(
    private val keySettings: KeySettings,
    private val typeRepository: TypeRepository
): ViewModel() {
    init {
        viewModelScope.launch {
            if (!keySettings.getIsSetDefaultTypes()){
                val types = mutableListOf<Type>()
                val defaultCategories = CategoryList.items.groupBy { it.typeId }
                defaultCategories.onEachIndexed { index, entry ->
                    val typeId = entry.key?:0
                    val typeName = CategoryList.getTypeStringById(typeId = typeId)
                    val typeColor = CategoryList.getColorByTypeId(id = typeId).toArgb()
                    types.add(
                        Type(
                            typeIdTimestamp = typeId,
                            name = typeName,
                            colorArgb = typeColor,
                            order = index,
                            categories = entry.value.mapIndexed { index, category ->
                                val description = CategoryList.getCategoryNameById(category.id.toLong())
                                category.copy(
                                    order = index,
                                    name = description
                                )
                            }
                        )
                    )
                }
                types.forEach {
                    typeRepository.insert(it)
                }
                keySettings.setIsSetDefaultTypes(true)
            }

            AdMobBannerController.setCloseAdMobBanner(
                isClose = keySettings.getCloseAdBanner()
            )


        }

    }
}