@file:OptIn(ExperimentalResourceApi::class)

package feature.add.presentation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import feature.add.presentation.components.CalculateLayout
import feature.add.presentation.components.CostTypeSelect
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.model.CategoryUi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalResourceApi::class,
    ExperimentalLayoutApi::class
)

class AddScreen: Screen {


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val addScreenModel = getScreenModel<AddScreenModel>()
        val state by addScreenModel.state.collectAsState()

        val bottomSheetState  = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        )

        val scope = rememberCoroutineScope()

        var currentCategoryUi by remember {
            mutableStateOf<CategoryUi?>(null)
        }

        var isShowBottomSheet by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(addScreenModel){
            addScreenModel.uiEvent.collectLatest {
                when(it){
                    AddUiEvent.Fail -> Unit
                    AddUiEvent.Success -> {
                        navigator.pop()
                    }
                }
            }
        }

        LaunchedEffect(state.categoryUi , Unit){
            currentCategoryUi = state.categoryUi
            println("state.categoryUi ${state.categoryUi}")
            scope.launch {
                if (currentCategoryUi == null){
                    println("hide")
                    isShowBottomSheet = false
                }else{
                    println("expand")
                    isShowBottomSheet = true
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AddTitleSection(
                modifier = Modifier.padding(horizontal = 8.dp),
                onCloseClick = {
                    navigator.pop()
                }
            )
            CostTypeSelect(
                modifier = Modifier.padding(8.dp),
                isIncome =  state.isIncome,
                onTypeChange = {
                    addScreenModel.onEvent(
                        AddEvent.OnTypeChange(it)
                    )
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ){
//                    if (state.recentlyCategoryItems.isNotEmpty()){
//                        item {
//                            Column {
//                                Text(
//                                    text = stringResource(Res.string.recently),
//                                    style = MaterialTheme.typography.titleSmall
//                                )
//                                FlowRow(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    maxItemsInEachRow = 4,
//                                ) {
//                                    state.recentlyItems.forEach { expense  ->
//                                        CategoryItem(
//                                            modifier = Modifier.weight(1f),
//                                            isClicked = categoryState.isClick,
//                                            categoryUi = categoryState,
//                                            onItemClick = {
//                                                onEvent(
//                                                    AddNewExpenseEvent.OnRecentlyItemSelected(
//                                                        category = categoryState.category,
//                                                        description = categoryState.category.name.ifEmpty { "" }
//                                                    )
//                                                )
//                                                addScreenModel.onEvent(
//                                                    AddEvent.OnItemSelected(
//                                                        categoryUi = categoryState,
//                                                        description =
//                                                    )
//                                                )
//                                                scope.launch {
//                                                    bottomSheetScaffoldState.bottomSheetState.expand()
//                                                }
//                                            }
//                                        )
//
//                                    }
//                                    repeat(4 - state.recentlyCategories.size % 4) {
//                                        Spacer(modifier = Modifier.weight(1f))
//                                    }
//                                }
//                                Spacer(modifier = Modifier.height(spacer.normal))
//                            }
//                        }
//                    }

                state.categoryItems.groupBy { it.typeId }.toList().sortedBy {
                    it.first
                }.forEach { (typeId, category) ->
                    item {
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
                                category.forEach {categoryState ->
                                    val categoryNameRes = CategoryList.getCategoryDescriptionById(categoryState.categoryId)
                                    CategoryItem(
                                        modifier = Modifier.weight(1f),
                                        isClicked = categoryState.isClick,
                                        categoryUi = categoryState,
                                        onItemClick = {
                                            println("CategoryItem Click ${categoryState}")
                                            addScreenModel.onEvent(
                                                AddEvent.OnItemSelected(
                                                    categoryUi = categoryState,
                                                    description = categoryNameRes
                                                )
                                            )
                                            isShowBottomSheet = true
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

        if (isShowBottomSheet){
            ModalBottomSheet(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = 8.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                scope.launch {
                                    if (bottomSheetState.currentValue == SheetValue.Expanded) {
                                        isShowBottomSheet = false
                                    }
                                }
                            }
                        )
                    },
                sheetState = bottomSheetState,
                dragHandle = null,
                scrimColor = Color.Transparent,
                onDismissRequest = { isShowBottomSheet = false },
                content = {
                    currentCategoryUi?.let {
                        CalculateLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 16.dp)
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
            )

        }



    }


}

@Composable
private fun AddTitleSection(
    modifier: Modifier,
    onCloseClick: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        IconButton(
            onClick = onCloseClick
        ){
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
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

        val description = CategoryList.getCategoryDescriptionById(
                categoryUi.categoryId
            )
        Texts.BodySmall(text = description )

    }
}