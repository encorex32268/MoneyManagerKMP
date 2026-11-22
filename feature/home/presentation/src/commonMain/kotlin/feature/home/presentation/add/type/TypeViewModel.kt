package feature.home.presentation.add.type

import AppLogger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.mapper.toType
import core.domain.mapper.toTypeUi
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TypeViewModel(
    private val repository: TypeRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    val state: StateFlow<TypesState> = repository.getTypes()
        .map { data ->
            val mapped = data.map { it.toTypeUi() }
            val items = mapped.filter { it.isShow }.sortedBy { it.order }
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
            is TypeAction.OnMoveUp -> onMoveUp(event.type)
            is TypeAction.OnNew -> onNew(event.type)
            is TypeAction.OnHide -> onHide(event.type)
            is TypeAction.OnDelete -> onDelete(event.type)
            is TypeAction.OnShow -> onShow(event.type)
            else -> Unit
        }
    }

    private fun onMoveUp(typeUi: TypeUi) {
        val current = state.value.items.toMutableList()
        val index = current.indexOfFirst { it.typeIdHex == typeUi.typeIdHex }
        if (index <= 0) return

        val moved = current.removeAt(index)
        current.add(index - 1, moved)

        viewModelScope.launch {
            repository.updateAllSortedTypes(
                current.mapIndexed { newIndex, item ->
                    item.copy(order = newIndex).toType()
                }
            )
        }
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
}
