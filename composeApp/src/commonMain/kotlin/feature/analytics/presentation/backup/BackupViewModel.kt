package feature.analytics.presentation.backup

import androidx.lifecycle.ViewModel

class BackupViewModel: ViewModel() {


    fun onEvent(event: BackupEvent){
        when(event){
            BackupEvent.OnBackup -> TODO()
            BackupEvent.OnImport -> TODO()
            else                 -> Unit
        }
    }
}