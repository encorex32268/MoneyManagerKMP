package feature.analytics.presentation.backup.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import core.ui.bglightColor
import core.ui.borderColor
import core.ui.textColor
import feature.analytics.presentation.backup.BackupEvent
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.backup_backup
import moneymanagerkmp.composeapp.generated.resources.backup_import
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackupCardSection(
    title: String,
    onEvent: (BackupEvent) -> Unit,
    backupImage: Painter,
    restoreImage: Painter,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth().padding(8.dp)
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(
                    width = 1.dp ,
                    color = MaterialTheme.colorScheme.borderColor(),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.bglightColor())
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
                    painter = backupImage,
                    contentDescription = "backup image",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.textColor())
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
                    painter = restoreImage,
                    contentDescription = "import image",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.textColor())
                )
                Text(
                    text = stringResource(Res.string.backup_import),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

    }
}