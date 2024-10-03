@file:OptIn(ExperimentalResourceApi::class, KoinExperimentalAPI::class)

package feature.home.presentation.edit

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.core.domain.model.Expense
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
import toMoneyString

@Composable
fun EditExpenseScreenRoot(
    expense: Expense,
    viewModel: EditExpenseViewModel = koinViewModel(),
    onGoBack: () -> Unit = {},
    onGotoAddScreen: () -> Unit = {}
){
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit){
        viewModel.onEvent(
            EditExpenseEvent.GetExpense(expense)
        )
    }
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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onGoBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
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
            OutlinedCard(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircleIcon(
                            modifier = Modifier.size(48.dp),
                            isClicked = true,
                            image = CategoryList.getCategoryIconById(it.categoryId.toLong()),
                            backgroundColor = CategoryList.getColorByTypeId(it.typeId.toLong())
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Texts.TitleSmall(
                            modifier = Modifier.weight(1f),
                            text = it.description
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            modifier = Modifier.weight(1f),
                            text = DateConverter.getStringDateFromLong(it.timestamp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                letterSpacing = 1.sp
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.baseline_attach_money_24),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            modifier = Modifier.weight(1f),
                            text = if (it.isIncome) it.cost.toMoneyString() else "-${it.cost.toMoneyString()}",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            CostTypeItem(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                isIncome = it.isIncome
                            )
                        }
                        IconButton(
                            onClick = onGotoAddScreen
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = null
                            )
                        }

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
