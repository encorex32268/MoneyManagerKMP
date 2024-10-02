package feature.home.add.type.category

sealed interface TypeCategoryEditUiEvent {
    data object OnBack: TypeCategoryEditUiEvent
    data object OnSavedShow: TypeCategoryEditUiEvent
}