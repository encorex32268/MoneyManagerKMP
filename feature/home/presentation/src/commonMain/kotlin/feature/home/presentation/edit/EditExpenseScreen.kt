@file:OptIn(
    ExperimentalResourceApi::class,
    KoinExperimentalAPI::class,
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)

package feature.home.presentation.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.domain.exts.formatAddZero
import core.domain.model.Expense
import core.presentation.CategoryList
import core.presentation.ObserveAsEvents
import core.presentation.components.CircleIcon
import core.presentation.components.DeleteDialog
import core.presentation.components.MoneyCard
import core.presentation.date.toDayOfWeekStringResource
import core.presentation.date.toLocalDateTime
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import kotlinx.coroutines.launch
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_sticky_note_24
import moneymanagerkmp.composeapp.generated.resources.cd_back
import moneymanagerkmp.composeapp.generated.resources.cd_delete
import moneymanagerkmp.composeapp.generated.resources.cd_edit
import moneymanagerkmp.composeapp.generated.resources.description
import moneymanagerkmp.composeapp.generated.resources.expense_detail
import moneymanagerkmp.composeapp.generated.resources.note_placeholder
import moneymanagerkmp.composeapp.generated.resources.save
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
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
    onGotoAddScreen: (Expense) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveAsEvents(
        events = viewModel.uiEvent,
        key1 = viewModel,
    ) {
        when (it) {
            EditExpenseUiEvent.OnBack -> onGoBack()
            is EditExpenseUiEvent.OnGoAddScreen -> onGotoAddScreen(it.expense)
            EditExpenseUiEvent.HideKeyboard -> {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        }
    }
    EditExpenseScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun EditExpenseScreen(
    state: EditExpenseState,
    onAction: (EditExpenseAction) -> Unit = {},
) {
    var isShowDeleteDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        containerColor = MoneyTheme.colors.bg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MoneyTheme.colors.bg,
                    titleContentColor = MoneyTheme.colors.ink,
                    navigationIconContentColor = MoneyTheme.colors.ink,
                    actionIconContentColor = MoneyTheme.colors.ink,
                ),
                title = {
                    Text(
                        text = stringResource(Res.string.expense_detail),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(EditExpenseAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.cd_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(EditExpenseAction.OnGoAddScreenClick) }) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = stringResource(Res.string.cd_edit),
                        )
                    }
                    IconButton(onClick = { isShowDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(Res.string.cd_delete),
                            tint = MoneyTheme.colors.budgetDanger,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        state.currentExpenseUi?.let { expenseUi ->
            val colorArgb = remember(expenseUi) {
                expenseUi.type?.colorArgb
                    ?: CategoryList.getColorByTypeId(expenseUi.typeId).toArgb()
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = MoneyTokens.ScreenHorizontalPadding, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                MoneyCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(18.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        CircleIcon(
                            modifier = Modifier.size(44.dp),
                            isClicked = true,
                            image = CategoryList.getCategoryIconById(expenseUi.categoryId.toLong()),
                            backgroundColor = Color(expenseUi.colorRGB ?: colorArgb),
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Text(
                                text = expenseUi.description,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = MoneyTheme.colors.ink,
                            )
                            expenseUi.type?.let { type ->
                                Text(
                                    text = type.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MoneyTheme.colors.muted,
                                )
                            }
                        }
                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MoneyTheme.colors.line,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TimestampSection(timestamp = expenseUi.timestamp)
                    Spacer(modifier = Modifier.height(8.dp))
                    CostSection(cost = expenseUi.cost)
                }

                ContentSection(
                    content = expenseUi.content,
                    onAction = onAction,
                    isShowSaveIcon = state.isShowSaveIcon,
                )
            }
        }
    }

    if (isShowDeleteDialog) {
        DeleteDialog(
            onConfirmButtonClick = { onAction(EditExpenseAction.OnDelete) },
            onDismissRequest = { isShowDeleteDialog = false },
        )
    }
}

@Composable
private fun CostSection(
    cost: Long,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            modifier = Modifier.width(20.dp),
            text = "$",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = MoneyTheme.colors.muted,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = cost.toMoneyString().removePrefix("$"),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            ),
            color = MoneyTheme.colors.ink,
        )
    }
}

@Composable
private fun TimestampSection(
    timestamp: Long,
) {
    val dayOfWeek = stringResource(timestamp.toLocalDateTime().toDayOfWeekStringResource())
    val date = remember(timestamp, dayOfWeek) {
        val localDateTime = timestamp.toLocalDateTime()
        if (localDateTime.hour == 0 && localDateTime.minute == 0 && localDateTime.second == 0 && localDateTime.nanosecond == 0) {
            "${localDateTime.monthNumber}/${localDateTime.dayOfMonth} ($dayOfWeek)"
        } else {
            "${localDateTime.monthNumber}/${localDateTime.dayOfMonth} ($dayOfWeek) ${localDateTime.hour.formatAddZero()}:${localDateTime.minute.formatAddZero()}:${localDateTime.second.formatAddZero()}"
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Outlined.DateRange,
            contentDescription = null,
            tint = MoneyTheme.colors.muted,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = date,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MoneyTheme.colors.ink,
        )
    }
}

@Composable
private fun ContentSection(
    modifier: Modifier = Modifier,
    content: String = "",
    isShowSaveIcon: Boolean,
    onAction: (EditExpenseAction) -> Unit,
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()

    MoneyCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(18.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = vectorResource(Res.drawable.baseline_sticky_note_24),
                    contentDescription = null,
                    tint = MoneyTheme.colors.muted,
                )
                Text(
                    text = stringResource(Res.string.description),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MoneyTheme.colors.muted,
                )
            }
            if (isShowSaveIcon) {
                Surface(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onAction(EditExpenseAction.OnSaveClick) },
                    shape = CircleShape,
                    color = MoneyTheme.colors.surfaceAlt,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        text = stringResource(Res.string.save),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MoneyTheme.colors.ink,
                    )
                }
            }
        }

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 110.dp)
                .padding(vertical = 6.dp)
                .bringIntoViewRequester(bringIntoViewRequester),
            value = content,
            onValueChange = {
                onAction(EditExpenseAction.OnContentChange(it))
                scope.launch {
                    bringIntoViewRequester.bringIntoView()
                }
            },
            decorationBox = { innerTextField ->
                Box {
                    if (content.trim().isEmpty()) {
                        Text(
                            text = stringResource(Res.string.note_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MoneyTheme.colors.muted,
                        )
                    }
                    innerTextField()
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.5.sp,
                lineHeight = 22.sp,
                color = MoneyTheme.colors.ink,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    scope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            ),
            cursorBrush = SolidColor(MoneyTheme.colors.ink),
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun EditExpenseScreenPreview() {
    app.presentation.AppTheme {
        EditExpenseScreen(state = EditExpenseState())
    }
}
