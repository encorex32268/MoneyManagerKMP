package app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.migration.RealmToRoomMigrator
import core.domain.KeySettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AppViewModel(
    private val keySettings: KeySettings,
    private val realmToRoomMigrator: RealmToRoomMigrator
) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state
        .onStart {
            realmToRoomMigrator.migrateIfNeeded()
            _state.update {
                it.copy(
                    isDarkMode = keySettings.getDarkLightMode(),
                    isMigrationReady = true
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(event: AppAction) {
        when (event) {
            AppAction.DarkLightChange -> {
                val currentMode = state.value.isDarkMode
                _state.update {
                    it.copy(
                        isDarkMode = !currentMode
                    )
                }
                keySettings.setDarkLightMode(isDark = !currentMode)
            }
        }
    }
}
