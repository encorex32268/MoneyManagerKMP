package feature.analytics.domain

import io.github.vinceglb.filekit.core.PlatformFile

interface BackupRepository {
    suspend fun backup(): BackupResult
    suspend fun restoreData(platformFile: PlatformFile): BackupResult
}