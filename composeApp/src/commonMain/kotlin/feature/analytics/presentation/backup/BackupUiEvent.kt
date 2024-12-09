package feature.analytics.presentation.backup

import feature.analytics.domain.util.backup.BackupError

sealed interface BackupUiEvent {
    data object TaskSuccess: BackupUiEvent
    data class SendError(
        val error: BackupError.Error
    ): BackupUiEvent
}