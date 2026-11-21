package feature.home.presentation.add.type

import AppLogger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.mapper.toType
import core.domain.mapper.toTypeUi
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TypeViewModel(
    private val repository: TypeRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val reorderedItems = MutableStateFlow<List<TypeUi>?>(null)

    val state: StateFlow<TypesState> = combine(
        repository.getTypes(),
        reorderedItems
    ) { data, draggedItems ->
        val mapped = data.map { it.toTypeUi() }
        val items = draggedItems ?: mapped.filter { it.isShow }.sortedBy { it.order }
        val itemsNotShowing = mapped.filter { !it.isShow }.sortedBy { it.order }
        AppLogger.d("TypeViewModel", "Items Showing $items")
        AppLogger.d("TypeViewModel", "Items itemsNotShowing $itemsNotShowing")
        TypesState(
            items = items,
            itemsNotShowing = itemsNotShowing
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = TypesState()
    )

    fun onAction(event: TypeAction) {
        when (event) {
            is TypeAction.OnItemMove -> onItemMove(event)
            is TypeAction.OnNew -> onNew(event.type)
            is TypeAction.OnHide -> onHide(event.type)
            is TypeAction.OnDelete -> onDelete(event.type)
            is TypeAction.OnShow -> onShow(event.type)
            is TypeAction.OnDragEnd -> onDragEnd()
            else -> Unit
        }
    }

    private fun onItemMove(event: TypeAction.OnItemMove) {
        val current = state.value.items.toMutableList()
        current.add(event.toItemPosition.index, current.removeAt(event.fromItemPosition.index))
        reorderedItems.value = current
    }

    private fun onNew(typeUi: TypeUi) {
        if (typeUi.name.isEmpty() || typeUi.colorArgb == 0) return
        viewModelScope.launch {
            repository.insert(type = typeUi.toType())
        }
    }

    private fun onHide(typeUi: TypeUi) {
        viewModelScope.launch {
            repository.update(type = typeUi.copy(isShow = false).toType())
        }
    }

    private fun onDelete(typeUi: TypeUi) {
        viewModelScope.launch {
            repository.delete(typeUi.toType())
            val expenses = expenseRepository
                .getExpenseByTypeId(typeUi.typeIdTimestamp)
                .first()
            expenses.forEach { expenseRepository.delete(it) }
        }
    }

    private fun onShow(typeUi: TypeUi) {
        viewModelScope.launch {
            repository.update(type = typeUi.copy(isShow = true).toType())
        }
    }

    private fun onDragEnd() {
        val itemsToSave = state.value.items
        viewModelScope.launch {
            repository.updateAllSortedTypes(
                itemsToSave.mapIndexed { index, typeUi ->
                    typeUi.copy(order = index).toType()
                }
            )
            reorderedItems.value = null
        }
    }
}
