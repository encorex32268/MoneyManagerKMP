@file:OptIn(ExperimentalResourceApi::class, KoinExperimentalAPI::class)

package feature.home.presentation.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.components.TwoButtonDialog
import feature.core.presentation.date.DateConverter
import feature.home.presentation.edit.components.CostTypeItem
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_attach_money_24
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_content
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import toMoneyString

@Composable
fun EditExpenseScreenRoot(
    expense: Expense,
    viewModel: EditExpenseViewModel = koinViewModel {
        parametersOf(expense)
    },
    onGoBack: () -> Unit = {},
    onGotoAddScreen: () -> Unit = {}
){
    val state by viewModel.state.collectAsState()
    EditExpenseScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onGoBack = onGoBack,
        onGotoAddScreen = onGotoAddScreen
    )
}


@Composable
fun EditExpenseScreen(
    state: EditExpenseState,
    onEvent: (EditExpenseEvent) -> Unit = {},
    onGoBack: () -> Unit = {},
    onGotoAddScreen: () -> Unit = {}
) {

    var isShowDeleteDialog by remember {
        mutableStateOf(false)
    }
    state.currentExpense?.let {
        val type = remember {
            state.typeItems.find { type ->
                type.typeIdTimestamp == it.typeId
            }
        }
        val colorArgb = remember {
            type?.colorArgb ?:CategoryList.getColorByTypeId(it.typeId).toArgb()
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ){
                    IconButton(
                        onClick = onGoBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                }
                Row {
                    IconButton(
                        onClick = onGotoAddScreen
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {
                            isShowDeleteDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null
                        )
                    }

                }

            }
            OutlinedCard(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    IconSection(
                        iconId = it.categoryId,
                        colorArgb = colorArgb,
                        description = it.description
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 8.dp)
                        ,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        GroupSection(type = type)
                        TimestampSection(
                            timestamp = it.timestamp
                        )
                        CostSection(
                            isIncome = it.isIncome,
                            cost = it.cost
                        )
                        CostTypeSection(
                            isIncome = it.isIncome
                        )
                    }
                }
            }
        }

    }
    if (isShowDeleteDialog){
        TwoButtonDialog(
            title = stringResource(Res.string.dialog_delete_title),
            content = stringResource(Res.string.dialog_delete_content),
            onConfirmButtonClick = {
                onEvent(EditExpenseEvent.OnDelete)
                onGoBack()
            },
            onDismissRequest = {
                isShowDeleteDialog = false
            }
        )
    }
}

@Composable
private fun CostTypeSection(
    modifier: Modifier = Modifier,
    isIncome: Boolean
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        CostTypeItem(
            isIncome = isIncome
        )
    }
}

@Composable
private fun CostSection(
    isIncome: Boolean,
    cost: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.baseline_attach_money_24),
            modifier = Modifier.size(16.dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Texts.TitleSmall(
            modifier = Modifier
                .weight(1f),
            text = if (isIncome) cost.toMoneyString() else "-${cost.toMoneyString()}",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun TimestampSection(
    timestamp: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Outlined.DateRange,
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
        Texts.TitleSmall(
            modifier = Modifier.weight(1f),
            text = DateConverter.getStringDateFromLong(timestamp),
            style = MaterialTheme.typography.bodySmall.copy(
                letterSpacing = 1.sp
            )
        )
    }
}

@Composable
private fun GroupSection(
    modifier: Modifier = Modifier,
    type: Type?
) {
    type?.let {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier.size(16.dp).background(
                    color = Color(it.colorArgb),
                    shape = CircleShape
                )
            )
            Spacer(Modifier.width(16.dp))
            Texts.TitleSmall(
                modifier = Modifier.weight(1f),
                text = it.name
            )
        }
    }
}

@Composable
private fun IconSection(
    modifier: Modifier = Modifier,
    iconId: Int,
    colorArgb: Int,
    description: String
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIcon(
            modifier = Modifier.size(36.dp),
            isClicked = true,
            image = CategoryList.getCategoryIconById(iconId.toLong()),
            backgroundColor = Color(colorArgb)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Texts.TitleSmall(
            modifier = Modifier.weight(1f),
            text = description
        )
    }

}
