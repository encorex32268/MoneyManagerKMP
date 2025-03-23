package feature.analytics.domain.util.backup

import core.presentation.UiText
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.backup_error_csv_data_empty
import moneymanagerkmp.composeapp.generated.resources.backup_error_downloads_path_empty
import moneymanagerkmp.composeapp.generated.resources.backup_error_json_parse
import moneymanagerkmp.composeapp.generated.resources.backup_error_restore_exception

sealed interface BackupError {
    enum class Error {
        DOWNLOADS_PATH_IS_EMPTY,
        CSV_DATA_IE_EMPTY,
        RESTORE_EXCEPTION,
        JSON_DECODE_ERROR
    }
}

fun BackupError.Error.toUiText(): UiText {
   return when(this){
        BackupError.Error.DOWNLOADS_PATH_IS_EMPTY -> UiText.StringResourceId(Res.string.backup_error_downloads_path_empty)
        BackupError.Error.CSV_DATA_IE_EMPTY       -> UiText.StringResourceId(Res.string.backup_error_csv_data_empty)
        BackupError.Error.RESTORE_EXCEPTION       -> UiText.StringResourceId(Res.string.backup_error_restore_exception)
        BackupError.Error.JSON_DECODE_ERROR       -> UiText.StringResourceId(Res.string.backup_error_json_parse)
    }
}