package app.presentation

sealed interface AppEvent {
    data object DarkLightChange: AppEvent
}