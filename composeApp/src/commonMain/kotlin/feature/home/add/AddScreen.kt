@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class,
    ExperimentalLayoutApi::class, KoinExperimentalAPI::class, ExperimentalMaterial3Api::class
)

package feature.home.add

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.home.add.components.CalculateLayout
import feature.home.add.components.CategoryItem
import feature.home.add.components.CostTypeSelect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.recently
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@Composable
fun AddScreenRoot(
    viewModel: AddViewModel = koinViewModel(),
    expense: Expense?=null,
    onGoBack: () -> Unit = {},
    onGoToCategoryEditClick: () -> Unit = {}
){
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit){
        viewModel.onEvent(
            AddEvent.SetupExpense(expense)
        )
    }
    LaunchedEffect(viewModel){
        viewModel.uiEvent.collectLatest {
            when(it){
                AddUiEvent.Success -> {
                    onGoBack()
                }
                else -> Unit
            }
        }
    }
    AddScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onGoBack = onGoBack,
        onGoToCategoryEditClick = onGoToCategoryEditClick
    )
}


@Composable
fun AddScreen(
    state: AddState,
    onEvent: (AddEvent) -> Unit = {},
    onGoBack: () -> Unit = {},
    onGoToCategoryEditClick: () -> Unit = {}
){
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    val bottomSheetState = rememberStandardBottomSheetState(
        skipHiddenState = false,
    )
    val bottomSheetScaffoldState  = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )
    val scope = rememberCoroutineScope()


    LaunchedEffect(state.categoryUi){
        scope.launch {
            if (state.categoryUi == null){
                bottomSheetScaffoldState.bottomSheetState.hide()
            }else{
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
        }
    }

    BottomSheetScaffold(
        containerColor = Color.White,
        sheetContainerColor = Color.White,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        scope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
                                bottomSheetScaffoldState.bottomSheetState.hide()
                            }
                        }
                        keyboard?.hide()
                        focusManager.clearFocus(true)
                    }
                )
            }
        ,
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit){
                        detectTapGestures(
                            onTap = {
                                keyboard?.hide()
                                focusManager.clearFocus(true)
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                DragHandle()
            }
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            CalculateLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 16.dp)
                    .pointerInput(Unit){
                        detectTapGestures(
                            onTap = {
                                keyboard?.hide()
                                focusManager.clearFocus(true)
                            }
                        )
                    }
                ,
                onItemClick = {
                    onEvent(
                        AddEvent.OnCostChange(it)
                    )
                },
                onDelete = {
                    onEvent(
                        AddEvent.OnDeleteTextClick
                    )
                },
                onOkClick = {
                    onEvent(
                        AddEvent.OnSaveClick
                    )
                },
                onValueChange = {
                    onEvent(
                        AddEvent.OnDescriptionChange(it)
                    )
                },
                onDateSelected = {
                    onEvent(
                        AddEvent.OnSelectedDate(it)
                    )
                } ,
                month = state.monthNumber.toString(),
                day = state.dayOfMonth.toString(),
                state = state
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CostTypeSelect(
                modifier = Modifier.padding(8.dp),
                isIncome =  state.isIncome,
                onTypeChange = {
                    onEvent(
                        AddEvent.OnTypeChange(it)
                    )
                },
                onCloseClick = onGoBack,
                onGoToCategoryEditClick = onGoToCategoryEditClick
            )
            if (state.isLoading){
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        color = Color.Black
                    )
                }
            }else{
                ItemSection(state, onEvent, scope, bottomSheetScaffoldState)
            }

        }
    }
}

@Composable
private fun ItemSection(
    state: AddState,
    onEvent: (AddEvent) -> Unit,
    scope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        if (state.recentlyItems.isNotEmpty()) {
            Column {
                Text(
                    text = stringResource(Res.string.recently),
                    style = MaterialTheme.typography.titleSmall
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 4,
                ) {
                    val categories = state.recentlyItems.first().categories
                    categories.forEach { categoryUi ->
                        val categoryNameRes =
                            CategoryList.getCategoryDescriptionById(categoryUi.id.toLong())
                        CategoryItem(
                            modifier = Modifier.weight(1f),
                            isClicked = categoryUi.isClick,
                            categoryUi = categoryUi,
                            onItemClick = {
                                onEvent(
                                    AddEvent.OnItemSelected(
                                        categoryUi = categoryUi,
                                        description = categoryUi.name.ifEmpty { categoryNameRes },
                                        isRecently = true
                                    )
                                )
                                scope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            }
                        )
                    }
                    repeat(4 - categories.size % 4) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

        }
        state.types.forEach {
            Column {
                Texts.TitleSmall(
                    text = it.name
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 4,
                ) {
                    it.categories.forEach { categoryUi ->
                        CategoryItem(
                            modifier = Modifier.weight(1f),
                            isClicked = categoryUi.isClick,
                            categoryUi = categoryUi,
                            onItemClick = {
                                onEvent(
                                    AddEvent.OnItemSelected(
                                        categoryUi = categoryUi,
                                        description = categoryUi.name
                                    )
                                )
                                scope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            }
                        )

                    }
                    repeat(4 - it.categories.size % 4) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

        }

    }
}