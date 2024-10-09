package feature.home.presentation.add.type


data class TypesState(
    val items: List<TypeUi> = emptyList(),
    val itemsNotShowing: List<TypeUi> = emptyList(),
    val isSaving: Boolean = false
)