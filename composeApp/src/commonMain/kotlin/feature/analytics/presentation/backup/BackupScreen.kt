@file:OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)

package feature.analytics.presentation.backup

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.presentation.ObserveAsEvents
import core.presentation.UiText
import core.presentation.components.OneButtonDialog
import feature.analytics.domain.util.backup.toUiText
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.backup_backup
import moneymanagerkmp.composeapp.generated.resources.backup_import
import moneymanagerkmp.composeapp.generated.resources.error
import moneymanagerkmp.composeapp.generated.resources.failed_snackbar
import moneymanagerkmp.composeapp.generated.resources.success_snackbar
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun BackupScreenRoot(
    onBackClick: () -> Unit = {},
    viewModel: BackupViewModel = koinViewModel()
) {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val singleFilePicker = rememberFilePickerLauncher(
        type = PickerType.File(listOf("txt")),
        mode = PickerMode.Single,
    ){ platformFile ->
        platformFile?.let {
            viewModel.onEvent(
                BackupEvent.OnImport(platformFile)
            )
        }
    }
    var uiText by remember {
        mutableStateOf<UiText?>(null)
    }
    ObserveAsEvents(events = viewModel.uiEvent , viewModel){ event ->
        when(event){
            is BackupUiEvent.SendError -> {
                scope.launch {
                    hostState.showSnackbar(
                        getString(Res.string.failed_snackbar),
                        duration = SnackbarDuration.Short
                    )
                }
                uiText = event.error.toUiText()
            }
            BackupUiEvent.TaskSuccess  -> {
                scope.launch {
                    hostState.showSnackbar(
                        getString(Res.string.success_snackbar),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState)
        },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Backup Back"
                        )
                    }
                }
            )
        }
    ){
        BackupScreen(
            modifier = Modifier.fillMaxSize().padding(it),
            onEvent = { event ->
                when(event){
                    BackupEvent.OnBackClick -> onBackClick()
                    is BackupEvent.OnImport -> {
                        singleFilePicker.launch()
                    }
                    else               -> viewModel.onEvent(event)
                }
            },
        )
        uiText?.let {
            val errorMessage = it.asString()
            OneButtonDialog(
                title = stringResource(Res.string.error),
                content = errorMessage,
                onDismissRequest = {
                    uiText = null
                }
            )
        }

    }


}

@Composable
fun BackupScreen(
    modifier: Modifier = Modifier,
    onEvent: (BackupEvent) -> Unit = {}
) {
    Column(
        modifier = modifier
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(
                    width = 1.dp ,
                    color = Color.Black ,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .padding(32.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Column (
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        onClick = {
                            onEvent(BackupEvent.OnBackup)
                        }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Image(
                    painter = painterResource(Res.drawable.backup_backup),
                    contentDescription = "backup image"
                )
                Text(
                    text = stringResource(Res.string.backup_backup),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onEvent(BackupEvent.OnImport(null))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Image(
                    painter = painterResource(Res.drawable.backup_import),
                    contentDescription = "import image"
                )
                Text(
                    text = stringResource(Res.string.backup_import),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

}

