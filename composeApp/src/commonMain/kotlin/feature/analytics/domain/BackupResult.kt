package feature.analytics.domain

import feature.analytics.domain.util.backup.BackupError

data class BackupResult(
    val error: BackupError?=null
)