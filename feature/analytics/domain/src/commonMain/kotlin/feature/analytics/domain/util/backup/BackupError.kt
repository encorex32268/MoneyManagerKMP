package feature.analytics.domain.util.backup

sealed interface BackupError {
    enum class LocalError : BackupError {
        DOWNLOADS_PATH_IS_EMPTY,
        CSV_DATA_IE_EMPTY,
        RESTORE_EXCEPTION,
        JSON_DECODE_ERROR
    }
    //Network Backup ...
}
