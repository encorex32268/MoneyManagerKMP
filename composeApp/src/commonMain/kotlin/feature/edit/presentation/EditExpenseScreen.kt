@file:OptIn(ExperimentalResourceApi::class)

package feature.edit.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.ScreenTransitionContent
import feature.add.presentation.AddScreen
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.components.TwoButtonDialog
import feature.core.presentation.date.DateConverter.getStringDateFromLong
import feature.core.ui.light_CorrectColorContainer
import feature.core.ui.light_ErrorColorContainer
import feature.core.ui.light_onErrorColor
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_attach_money_24
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_content
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_title
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import toMoneyString


class EditExpenseScreen(
    private val expense: Expense
): Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val editExpenseScreenModel = navigator.getNavigatorScreenModel<EditExpenseScreenModel>()
        val state by editExpenseScreenModel.state.collectAsState()
        var isShowDeleteDialog by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(Unit){
            editExpenseScreenModel.onEvent(
                EditExpenseEvent.GetExpense(expense)
            )
        }
        state?.let { expenseState ->
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
                        onClick = {
                            navigator.pop()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = null)
                    }

                    IconButton(
                        onClick = {
                            isShowDeleteDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null)
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
                                image = CategoryList.getCategoryIconById(expenseState.categoryId),
                                backgroundColor = CategoryList.getColorByCategory(expenseState.typeId)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier.weight(1f),
                                text = expenseState.description,
                                style = MaterialTheme.typography.bodySmall
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
                                text = getStringDateFromLong(expenseState.timestamp),
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
                                text = if (expenseState.isIncome) expenseState.cost.toMoneyString() else "-${expenseState.cost.toMoneyString()}",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier.weight(1f)
                            ){
                                CostTypeItem(
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    isIncome = expenseState.isIncome
                                )
                            }
                            IconButton(
                                onClick = {
                                    navigator.push(
                                        AddScreen(
                                            expenseState
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = null)
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
                    editExpenseScreenModel.onEvent(
                        event = EditExpenseEvent.DeleteExpense(expense)
                    )
                    navigator.pop()
                },
                onDismissRequest = {
                    isShowDeleteDialog = false
                }
            )
        }
    }

}


@Composable
private fun CostTypeItem(
    modifier: Modifier = Modifier,
    isIncome: Boolean,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier
            .background(
                color = if (isIncome) {
                    light_CorrectColorContainer
                } else {
                    light_ErrorColorContainer
                },
                shape = shape
            )
        ,
        contentAlignment = Alignment.Center
    ){
        Texts.BodyMedium(
            modifier = Modifier.padding(4.dp),
            text =  stringResource(if (isIncome) Res.string.income else Res.string.expense),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
