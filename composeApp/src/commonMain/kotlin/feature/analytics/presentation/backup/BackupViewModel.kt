package feature.analytics.presentation.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.model.Category
import core.domain.model.Expense
import core.domain.model.Type
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
import core.presentation.date.DateConverter
import core.presentation.date.toStringDateYMDByTimestamp
import de.halfbit.csv.Csv
import de.halfbit.csv.buildCsv
import de.halfbit.csv.parseCsv
import feature.analytics.domain.BackupRepository
import feature.analytics.domain.util.backup.BackupError
import getDownloadsPath
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BackupViewModel(
    private val backupRepository: BackupRepository
) : ViewModel() {


    private val _uiEvent = Channel<BackupUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: BackupEvent) {
        when (event) {
            BackupEvent.OnBackup -> backup()
            is BackupEvent.OnImport -> {
                event.platformFile?.let {
                    restoreData(it)
                }
            }

            else -> Unit
        }
    }


    private fun backup() {
        viewModelScope.launch {
            val result = backupRepository.backup()
            if (result.error == null){
                _uiEvent.send(BackupUiEvent.TaskSuccess)
            }else{
                _uiEvent.send(BackupUiEvent.SendError(result.error))
            }
        }
    }

    private fun restoreData(
        platformFile: PlatformFile
    ) {
        viewModelScope.launch {
            val result = backupRepository.restoreData(platformFile)
            if (result.error == null){
                _uiEvent.send(BackupUiEvent.TaskSuccess)
            }else{
                _uiEvent.send(BackupUiEvent.SendError(result.error))
            }
        }
    }


}