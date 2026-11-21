package feature.analytics.presentation.backup

import io.github.vinceglb.filekit.core.PlatformFile

sealed interface BackupAction {
    data object OnBackup: BackupAction
    data class OnImport(
        val platformFile: PlatformFile?
    ): BackupAction
    data object OnBackClick: BackupAction
}