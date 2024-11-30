@file:OptIn(KoinExperimentalAPI::class)

package feature.analytics.presentation.backup

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.backup_backup
import moneymanagerkmp.composeapp.generated.resources.backup_import
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun BackupScreenRoot(
    onBackClick: () -> Unit = {},
    viewModel: BackupViewModel = koinViewModel()
) {
    BackupScreen(
        modifier = Modifier.fillMaxSize(),
        onEvent = { event ->
            when(event){
                BackupEvent.OnBack -> onBackClick()
                else               -> Unit
            }
            viewModel.onEvent(event)
        }
    )
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
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
            contentDescription = "Backup Back"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .heightIn(min = 150.dp , max = 300.dp)
                .border(
                    width = 1.dp ,
                    color = Color.Black ,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Column (
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                    onEvent(BackupEvent.OnBackup)
                },
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
                    onEvent(BackupEvent.OnImport)
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

@Composable
private fun IconText(

){

}
