package feature.analytics.presentation.backup

sealed interface BackupEvent {
    data object OnBackup: BackupEvent
    data object OnImport: BackupEvent
    data object OnBack: BackupEvent
}