package app.presentation

data class AppState(
    val isDarkMode: Boolean = false,
    val isMigrationReady: Boolean = false,
    val showDatabaseUpdateNotice: Boolean = false
)
