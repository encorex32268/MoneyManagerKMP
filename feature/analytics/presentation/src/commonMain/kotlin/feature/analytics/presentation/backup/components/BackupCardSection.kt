package feature.analytics.presentation.backup.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import core.presentation.components.MoneyCard
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import feature.analytics.presentation.backup.BackupAction
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.backup_backup
import moneymanagerkmp.composeapp.generated.resources.backup_import
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackupCardSection(
    title: String,
    onAction: (BackupAction) -> Unit,
    backupImage: Painter,
    restoreImage: Painter,
    modifier: Modifier = Modifier,
) {
    MoneyCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MoneyTokens.ScreenHorizontalPadding),
        contentPadding = PaddingValues(18.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MoneyTheme.colors.ink,
        )
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BackupActionTile(
                modifier = Modifier.weight(1f),
                image = backupImage,
                label = stringResource(Res.string.backup_backup),
                onClick = { onAction(BackupAction.OnBackup) },
            )
            BackupActionTile(
                modifier = Modifier.weight(1f),
                image = restoreImage,
                label = stringResource(Res.string.backup_import),
                onClick = { onAction(BackupAction.OnImport(null)) },
            )
        }
    }
}

@Composable
private fun BackupActionTile(
    image: Painter,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MoneyTheme.colors.bg)
            .clickable(onClick = onClick)
            .padding(vertical = 22.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = image,
            contentDescription = label,
            colorFilter = ColorFilter.tint(color = MoneyTheme.colors.ink),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MoneyTheme.colors.ink,
        )
    }
}
