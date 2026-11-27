package app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.KeySettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val keySettings: KeySettings
) : ViewModel() {

    val state: StateFlow<AppState> = keySettings.observeDarkLightMode()
        .map { isDarkMode ->
            AppState(isDarkMode = isDarkMode)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AppState()
        )

    fun onAction(event: AppAction) {
        when (event) {
            AppAction.DarkLightChange -> {
                val targetMode = !state.value.isDarkMode
                viewModelScope.launch {
                    keySettings.setDarkLightMode(isDark = targetMode)
                }
            }
        }
    }
}
