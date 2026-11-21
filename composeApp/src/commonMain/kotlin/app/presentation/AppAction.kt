package app.presentation

sealed interface AppAction {
    data object DarkLightChange: AppAction
}