package feature.analytics.presentation.backup

import io.github.vinceglb.filekit.core.PlatformFile

sealed interface BackupEvent {
    data object OnBackup: BackupEvent
    data class OnImport(
        val platformFile: PlatformFile?
    ): BackupEvent
    data object OnBackClick: BackupEvent
}