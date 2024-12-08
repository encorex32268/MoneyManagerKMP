@file:OptIn(KoinExperimentalAPI::class)

package feature.analytics.presentation.backup

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import feature.analytics.domain.util.backup.BackupError
import feature.analytics.domain.util.backup.toUiText
import feature.analytics.presentation.AnalyticsEvent
import feature.core.presentation.ObserveAsEvents
import feature.core.presentation.UiText
import feature.core.presentation.components.OneButtonDialog
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.backup_backup
import moneymanagerkmp.composeapp.generated.resources.backup_backup_icon
import moneymanagerkmp.composeapp.generated.resources.backup_import
import moneymanagerkmp.composeapp.generated.resources.error
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun BackupScreenRoot(
    onBackClick: () -> Unit = {},
    viewModel: BackupViewModel = koinViewModel()
) {
    val singleFilePicker = rememberFilePickerLauncher(
        type = PickerType.File()
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
                uiText = event.error.toUiText()
            }
        }
    }

    BackupScreen(
        modifier = Modifier.fillMaxSize(),
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

@Composable
fun BackupScreen(
    modifier: Modifier = Modifier,
    onEvent: (BackupEvent) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        IconButton(
            onClick = {
                onEvent(BackupEvent.OnBackClick)
            }
        ){
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = "Backup Back"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
                    text = "Backup"
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
                    text = "Import"
                )
            }
        }
    }

}

