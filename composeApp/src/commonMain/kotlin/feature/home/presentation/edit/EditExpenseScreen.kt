@file:OptIn(ExperimentalResourceApi::class, KoinExperimentalAPI::class,
    ExperimentalMaterial3Api::class
)

package feature.home.presentation.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.presentation.CategoryList
import feature.core.presentation.ObserveAsEvents
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.components.TwoButtonDialog
import feature.core.presentation.date.toDayOfWeekStringResource
import feature.core.presentation.date.toLocalDateTime
import feature.core.presentation.noRippleClick
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_attach_money_24
import moneymanagerkmp.composeapp.generated.resources.baseline_sticky_note_24
import moneymanagerkmp.composeapp.generated.resources.description
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_content
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_title
import moneymanagerkmp.composeapp.generated.resources.expense_detail
import moneymanagerkmp.composeapp.generated.resources.notosanslao_regular
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
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
    onGotoAddScreen: (Expense) -> Unit = {}
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveAsEvents(
        events = viewModel.uiEvent,
        key1 = viewModel
    ){
        when(it){
            EditExpenseUiEvent.OnBack -> onGoBack()
            is EditExpenseUiEvent.OnGoAddScreen -> {
                onGotoAddScreen(
                    it.expense
                )
            }
        }
    }
    EditExpenseScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}


@Composable
fun EditExpenseScreen(
    state: EditExpenseState,
    onEvent: (EditExpenseEvent) -> Unit = {}
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var isShowDeleteDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.expense_detail),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(EditExpenseEvent.OnBackClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(EditExpenseEvent.OnGoAddScreenClick)
                        }
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
            )
        }
    ){ paddingValues ->
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
                    .noRippleClick {
                        keyboard?.hide()
                        focusManager.clearFocus()
                    }
                    .verticalScroll(rememberScrollState())
                ,
            ) {
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
                        HorizontalDivider(
                            modifier = Modifier.padding(8.dp)
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
                            CostSection(cost = it.cost)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                ContentSection(
                    content = state.currentExpense.content,
                    onValueChange = {
                        onEvent(
                            EditExpenseEvent.OnContentChange(it)
                        )
                    }
                )
            }

        }

    }
    if (isShowDeleteDialog){
        TwoButtonDialog(
            title = stringResource(Res.string.dialog_delete_title),
            content = stringResource(Res.string.dialog_delete_content),
            onConfirmButtonClick = {
                onEvent(EditExpenseEvent.OnDelete)
            },
            onDismissRequest = {
                isShowDeleteDialog = false
            }
        )
    }
}


@Composable
private fun CostSection(
    cost: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(Res.drawable.baseline_attach_money_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Texts.TitleSmall(
            modifier = Modifier.weight(1f),
            text = cost.toMoneyString()
        )
    }
}

@Composable
private fun TimestampSection(
    timestamp: Long
) {
    val dayOfWeek = stringResource(timestamp.toLocalDateTime().toDayOfWeekStringResource())
    val date = remember(timestamp){
        val localDateTime = timestamp.toLocalDateTime()
        "${localDateTime.monthNumber}/${localDateTime.dayOfMonth} ${dayOfWeek}"
    }
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
            text = date
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

@Composable
private fun ContentSection(
    modifier: Modifier = Modifier,
    content: String = "",
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(Res.font.notosanslao_regular)),
        fontSize = 12.sp
    )
){
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(horizontal = 24.dp)
        ){
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = vectorResource(Res.drawable.baseline_sticky_note_24),
                contentDescription = null,
                tint = Color.Black
            )
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(16.dp),
                value = content,
                onValueChange = onValueChange,
                decorationBox = { it ->
                    if (content.trim().isEmpty()){
                        Text(
                            text = stringResource(Res.string.description),
                            style = textStyle,
                            color = Color.LightGray
                        )
                    }
                    it()
                },
                textStyle = textStyle,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )

        }
    }
}

