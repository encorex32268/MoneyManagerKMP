package feature.home.presentation.add.type.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import feature.core.domain.mapper.toType
import feature.core.domain.model.Type
import feature.core.domain.repository.TypeRepository
import feature.home.presentation.add.type.TypeUi
import feature.home.presentation.add.type.TypeUiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TypeCategoryEditViewModel(
    private val typeUi: TypeUi,
    private val repository: TypeRepository
): ViewModel() {

    private val _state = MutableStateFlow(TypeCategoryEditState(typeUi = typeUi))
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<TypeCategoryEditUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private val currentTypeUi = typeUi


    fun onEvent(event: TypeCategoryEditEvent){
        when(event){
            TypeCategoryEditEvent.OnBackClick -> {
                viewModelScope.launch {
                    val json = Json
                    val stateValueStringJson = json.encodeToString(state.value.typeUi.toType())
                    val currentValueStringJson = json.encodeToString(currentTypeUi.toType())

                    if (stateValueStringJson != currentValueStringJson){
                        viewModelScope.launch {
                            val updateData = state.value.typeUi.copy(
                                categories = state.value.typeUi.categories.mapIndexed { index, categoryUi ->
                                    categoryUi.copy(
                                        order = index,
                                        typeId = state.value.typeUi.typeIdTimestamp,
                                        colorArgb = state.value.typeUi.colorArgb
                                    )
                                },
                            )
                            repository.update(
                                type =updateData.toType()
                            )
                        }
                    }
                    _uiEvent.send(TypeCategoryEditUiEvent.OnBack)
                }
            }
            TypeCategoryEditEvent.OnSaveClick   -> {
                val updateData = state.value.typeUi.copy(
                    categories = state.value.typeUi.categories.mapIndexed { index, categoryUi ->
                        categoryUi.copy(
                            order = index,
                            typeId = state.value.typeUi.typeIdTimestamp,
                            colorArgb = state.value.typeUi.colorArgb
                        )
                    }
                )
                viewModelScope.launch {
                    repository.update(
                        type =updateData.toType()
                    )
                    _uiEvent.send(TypeCategoryEditUiEvent.OnSavedShow)
                }

            }
            is TypeCategoryEditEvent.OnItemAdd ->{
                if (event.categoryUi.name.trim().isEmpty()) return
                val newItems = state.value.typeUi.categories.toMutableList().apply {
                    add(event.categoryUi)
                }.mapIndexed { index, categoryUi ->
                    categoryUi.copy(
                        order = index
                    )
                }
                _state.update {
                    it.copy(
                        typeUi = state.value.typeUi.copy(
                            categories = newItems
                        )
                    )
                }

            }
            is TypeCategoryEditEvent.OnItemRemove ->{
                val newItems = state.value.typeUi.categories.toMutableList().apply {
                    remove(event.categoryUi)
                }.mapIndexed { index, categoryUi ->
                    categoryUi.copy(
                        order = index
                    )
                }
                _state.update {
                    it.copy(
                        typeUi = state.value.typeUi.copy(
                            categories = newItems
                        )
                    )
                }
            }
            is TypeCategoryEditEvent.OnItemMove -> {
                var newItems = state.value.typeUi.categories
                newItems = newItems.toMutableList().apply {
                    add(event.toItemPosition.index, removeAt(event.fromItemPosition.index))
                }
                _state.update {
                    it.copy(
                        typeUi = state.value.typeUi.copy(
                            categories = newItems
                        )
                    )
                }
            }

            is TypeCategoryEditEvent.OnTypeEdit -> {
                _state.update {
                    it.copy(
                        typeUi = state.value.typeUi.copy(
                            name = event.name,
                            colorArgb = event.colorArgb
                        )
                    )
                }
            }
        }
    }
}