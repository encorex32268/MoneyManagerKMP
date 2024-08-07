@file:OptIn(ExperimentalResourceApi::class)

package feature.presentation.add

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import feature.presentation.add.components.CalculateLayout
import feature.presentation.add.components.CostTypeSelect
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.model.CategoryUi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.recently
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalResourceApi::class,
    ExperimentalLayoutApi::class
)

class AddScreen(
    private val expense: Expense?
): Screen {

    @Composable
    override fun Content() {
        val keyboard = LocalSoftwareKeyboardController.current
        val navigator = LocalNavigator.currentOrThrow
        val addScreenModel = getScreenModel<AddScreenModel>()
        val state by addScreenModel.state.collectAsState()

        val bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false,
        )
        val bottomSheetScaffoldState  = rememberBottomSheetScaffoldState(
            bottomSheetState = bottomSheetState
        )
        val scope = rememberCoroutineScope()

        val correctList = remember {
            derivedStateOf {
                state.categoryItems
                    .groupBy { it.typeId }
                    .toList()
                    .sortedBy {
                        it.first
                    }
            }
        }

        val bottomSheetControl by remember {
            derivedStateOf { state.categoryUi }
        }

        LaunchedEffect(Unit){
            addScreenModel.onEvent(
                AddEvent.SetupExpense(expense)
            )
        }
        LaunchedEffect(addScreenModel){
            addScreenModel.uiEvent.collectLatest {
                when(it){
                    AddUiEvent.Fail    -> Unit
                    AddUiEvent.Success -> {
                        navigator.pop()
                    }
                }
            }
        }

        LaunchedEffect(bottomSheetControl){
            scope.launch {
                if (bottomSheetControl == null){
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
                                 }
                             )
                         },
                     contentAlignment = Alignment.TopCenter
                 ) {
                     BottomSheetDefaults.DragHandle()
                 }
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContent = {
                bottomSheetControl?.let {
                    CalculateLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 16.dp)
                            .pointerInput(Unit){
                                detectTapGestures(
                                    onTap = {
                                        keyboard?.hide()
                                    }
                                )
                            }
                        ,
                        onItemClick = {
                            addScreenModel.onEvent(
                                AddEvent.OnCostChange(it)
                            )
                        },
                        onDelete = {
                            addScreenModel.onEvent(
                                AddEvent.OnDeleteTextClick
                            )
                        },
                        onOkClick = {
                            addScreenModel.onEvent(
                                AddEvent.OnSaveClick
                            )
                        },
                        onValueChange = {
                            addScreenModel.onEvent(
                                AddEvent.OnDescriptionChange(it)
                            )
                        },
                        onDateSelected = {
                            addScreenModel.onEvent(
                                AddEvent.OnSelectedDate(it)
                            )
                        } ,
                        month = state.monthNumber.toString(),
                        day = state.dayOfMonth.toString(),
                        state = state
                    )

                }
            }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                scope.launch {
                                    if (bottomSheetScaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
                                        bottomSheetScaffoldState.bottomSheetState.hide()
                                    }
                                }
                                keyboard?.hide()
                            }
                        )
                    }
            ) {
                CostTypeSelect(
                    modifier = Modifier.padding(8.dp),
                    isIncome =  state.isIncome,
                    onTypeChange = {
                        addScreenModel.onEvent(
                            AddEvent.OnTypeChange(it)
                        )
                    },
                    onCloseClick = {
                        navigator.pop()
                    }
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    if (state.recentlyCategoryItems.isNotEmpty()) {
                        Column {
                            Text(
                                text = stringResource(Res.string.recently),
                                style = MaterialTheme.typography.titleSmall
                            )
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                maxItemsInEachRow = 4,
                            ) {
                                state.recentlyCategoryItems.forEach { categoryUi ->
                                    val categoryNameRes =
                                        CategoryList.getCategoryDescriptionById(categoryUi.categoryId)
                                    CategoryItem(
                                        modifier = Modifier.weight(1f),
                                        isClicked = categoryUi.isClick,
                                        categoryUi = categoryUi,
                                        onItemClick = {
                                            addScreenModel.onEvent(
                                                AddEvent.OnItemSelected(
                                                    categoryUi = categoryUi,
                                                    description = categoryUi.name
                                                        ?: categoryNameRes,
                                                    isRecently = true
                                                )
                                            )
                                            scope.launch {
                                                bottomSheetScaffoldState.bottomSheetState.expand()
                                            }
                                        }
                                    )

                                }
                                repeat(4 - state.recentlyCategoryItems.size % 4) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }

                    }

                    correctList.value.forEach { (typeId, category) ->
                        Column {
                            Texts.TitleSmall(
                                text = CategoryList.getTypeStringByTypeId(
                                    typeId
                                )
                            )
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                maxItemsInEachRow = 4,
                            ) {
                                category.forEach { categoryState ->
                                    val categoryNameRes =
                                        CategoryList.getCategoryDescriptionById(categoryState.categoryId)
                                    CategoryItem(
                                        modifier = Modifier.weight(1f),
                                        isClicked = categoryState.isClick,
                                        categoryUi = categoryState,
                                        onItemClick = {
                                            addScreenModel.onEvent(
                                                AddEvent.OnItemSelected(
                                                    categoryUi = categoryState,
                                                    description = categoryNameRes
                                                )
                                            )
                                            scope.launch {
                                                bottomSheetScaffoldState.bottomSheetState.expand()
                                            }
                                        }
                                    )

                                }
                                repeat(4 - category.size % 4) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                    }
                }

            }

        }
    }

}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    categoryUi: CategoryUi,
    isClicked: Boolean,
    onItemClick: () -> Unit = {}
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircleIcon(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(top = 8.dp)
                .size(48.dp)
            ,
            backgroundColor = CategoryList.getColorByCategory(categoryUi.typeId),
            image = CategoryList.getCategoryIconById(categoryUi.categoryId),
            isClicked = isClicked,
            id = categoryUi.categoryId,
            onItemClick = {
                onItemClick()
            },
        )

        val description = categoryUi.name?:CategoryList.getCategoryDescriptionById(
                categoryUi.categoryId
            )
        Texts.BodySmall(text = description )

    }
}