package core.domain.util

import core.presentation.date.DateConverter
import core.presentation.date.toTimestamp
import getDownloadsPath
import io.github.vinceglb.filekit.core.FileKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

object FileLogger {

    suspend fun write(content: String) =
        withContext(Dispatchers.IO){
            val downloadsPath = getDownloadsPath()
            if (downloadsPath.isBlank())return@withContext
            val nowTimestamp = DateConverter.getNowDate().toTimestamp()
            FileKit.saveFile(
                baseName = "MoneyManager_Logger${nowTimestamp}",
                extension = "txt",
                initialDirectory = downloadsPath,
                bytes = content.encodeToByteArray()
            )
        }
}