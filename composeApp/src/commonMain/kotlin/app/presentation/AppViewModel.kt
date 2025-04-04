package app.presentation

import AdMobBannerController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.KeySettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AppViewModel(
    private val keySettings: KeySettings,
): ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state
        .onStart {
            AdMobBannerController.setCloseAdMobBanner(
                isClose = keySettings.getCloseAdBanner()
            )
         _state.update {
                it.copy(
                    isDarkMode = keySettings.getDarkLightMode()
                )
         }}.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )


    fun onEvent(event: AppEvent){
        when(event){
            AppEvent.DarkLightChange -> {
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