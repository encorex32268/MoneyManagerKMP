package feature.home.add.type

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.core.domain.KeySettings
import feature.core.domain.mapper.toType
import feature.core.domain.mapper.toTypeUi
import feature.core.domain.model.Category
import feature.core.domain.model.Type
import feature.core.domain.repository.TypeRepository
import feature.core.presentation.CategoryList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TypeViewModel(
    private val repository: TypeRepository,
): ViewModel() {

    private val _state = MutableStateFlow(TypesState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<TypeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentItems = emptyList<TypeUi>()
    private var isCancel = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            repository.getTypes()
                .filter { !isCancel.value }
                .collectLatest { data ->
                val mappedData = data.map { it.toTypeUi() }.filter { it.isShow }
                val items = mappedData.sortedBy { it.order }

                _state.update {
                    it.copy(
                        items = items,
                    )
                }
                if (currentItems.isEmpty()){
                    currentItems = items
                }
            }
        }
    }

    fun onEvent(event: TypeEvent){
        when(event){
            is TypeEvent.OnItemMove -> {
                var data = state.value.items
                data = data.toMutableList().apply {
                    add(event.toItemPosition.index, removeAt(event.fromItemPosition.index))
                }
                _state.update {
                    it.copy(
                        items = data
                    )
                }
            }
            is TypeEvent.OnNew         -> {
                val typeUi = event.type
                viewModelScope.launch {
                    repository.insert(
                        typeUi.toType()
                    )
                }
            }

            is TypeEvent.OnHide        -> {
                val newItems = state.value.items.toMutableList().apply {
                    remove(event.type)
                }
                viewModelScope.launch {
                    repository.update(
                        type = event.type.copy(
                            isShow = false
                        ).toType()
                    )
                    _state.update {
                        it.copy(
                            items = newItems
                        )
                    }
                }
            }

            TypeEvent.OnBackClick      -> {
                viewModelScope.launch {
                    isCancel.update { true }
                    val items = state.value.items
                    val dataOrder = items.map { it.order }.toTypedArray()
                    val itemsOrder = currentItems.map { it.order }.toTypedArray()

                    if (!dataOrder.contentEquals(itemsOrder)){
                        val sortedList = state.value.items.mapIndexed { index, typeUi ->
                            typeUi.copy(
                                order = index
                            )
                        }
                        sortedList.forEach {
                            repository.update(it.toType())
                        }
                    }
                    _uiEvent.send(TypeUiEvent.OnBack)
                }
            }
        }
    }
}