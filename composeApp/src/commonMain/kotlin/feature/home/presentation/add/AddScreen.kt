@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class,
    ExperimentalLayoutApi::class, KoinExperimentalAPI::class, ExperimentalMaterial3Api::class
)

package feature.home.presentation.add

import AdMobBannerController
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.navigation.NavigationLayoutType
import feature.home.presentation.add.components.CalculateLayout
import feature.home.presentation.add.components.CategoryItem
import feature.home.presentation.add.components.CostTypeSelect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_edit_note_24
import moneymanagerkmp.composeapp.generated.resources.recently
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf


@Composable
fun AddScreenRoot(
    expense: Expense?=null,
    viewModel: AddViewModel = koinViewModel {
        parametersOf(expense)
    },
    onGoBack: () -> Unit = {},
    onGoToCategoryEditClick: () -> Unit = {},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
){
    val state by viewModel.state.collectAsStateWithLifecycle()
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
    if (navigationLayoutType == NavigationLayoutType.BOTTOM_NAVIGATION){
        AddScreen(
            state = state,
            onEvent = { event ->
                when(event){
                    AddEvent.OnBack                  -> onGoBack()
                    AddEvent.OnGoToCategoryEditClick -> onGoToCategoryEditClick()
                    else -> Unit
                }
                viewModel.onEvent(event)
            }
        )
    }else{
        AddScreenNaviRail(
            state = state,
            onEvent = { event ->
                when(event){
                    AddEvent.OnBack                  -> onGoBack()
                    AddEvent.OnGoToCategoryEditClick -> onGoToCategoryEditClick()
                    else -> Unit
                }
                viewModel.onEvent(event)
            }
        )
    }
}


@Composable
fun AddScreen(
    state: AddState,
    onEvent: (AddEvent) -> Unit = {},
    isDebug: Boolean = false,
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
        sheetShadowElevation = 8.dp,
        modifier = Modifier
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
                onEvent = onEvent,
                month = state.monthNumber.toString(),
                day = state.dayOfMonth.toString(),
                state = state
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                IconButton(
                    onClick = {
                        onEvent(AddEvent.OnBack)
                    }
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
                CostTypeSelect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 4.dp),
                    isIncome =  state.isIncome,
                    onTypeChange = {
                        onEvent(
                            AddEvent.OnTypeChange(it)
                        )
                    }
                )
                IconButton(
                    onClick = {
                        onEvent(AddEvent.OnGoToCategoryEditClick)
                    }
                ){
                    Icon(
                        imageVector = vectorResource(Res.drawable.baseline_edit_note_24),
                        contentDescription = null
                    )
                }


            }
            ItemSection(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                state = state,
                onEvent = onEvent,
                scope = scope,
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                isDebug = isDebug
            )

        }
    }
}



@Composable
fun AddScreenNaviRail(
    state: AddState,
    onEvent: (AddEvent) -> Unit = {},
    isDebug: Boolean = false,
){
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize().background(
            color = Color.White
        )
    ) {
        Row{
            Column(
                modifier = Modifier.weight(0.55f)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            onEvent(AddEvent.OnBack)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                    CostTypeSelect(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(top = 4.dp),
                        isIncome = state.isIncome,
                        onTypeChange = {
                            onEvent(
                                AddEvent.OnTypeChange(it)
                            )
                        }
                    )
                    IconButton(
                        onClick = {
                            onEvent(AddEvent.OnGoToCategoryEditClick)
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.baseline_edit_note_24),
                            contentDescription = null
                        )
                    }
                }
                ItemSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    state = state,
                    onEvent = onEvent,
                    isDebug = isDebug,
                    maxItemsInEachRow = 8
                )
            }
            state.categoryUi?.let {
                VerticalDivider()
                CalculateLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(0.45f)
                        .pointerInput(Unit){
                            detectTapGestures(
                                onTap = {
                                    keyboard?.hide()
                                    focusManager.clearFocus(true)
                                }
                            )
                        }
                    ,
                    onEvent = onEvent,
                    month = state.monthNumber.toString(),
                    day = state.dayOfMonth.toString(),
                    state = state,
                    navigationLayoutType = NavigationLayoutType.NAVIGATION_RAIL
                )

            }
        }


    }

}

@Composable
private fun ItemSection(
    modifier: Modifier = Modifier,
    state: AddState,
    onEvent: (AddEvent) -> Unit,
    scope: CoroutineScope?=null,
    bottomSheetScaffoldState: BottomSheetScaffoldState? = null,
    isDebug: Boolean = false,
    maxItemsInEachRow: Int = 4
) {

    LazyColumn(
        modifier = modifier
    ) {
        item {
            if (!isDebug) {
                AdMobBannerController.AdMobBannerCompose(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            if (state.recentlyItems?.categories?.isNotEmpty() == true) {
                Text(
                    text = stringResource(Res.string.recently),
                    style = MaterialTheme.typography.titleSmall
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = maxItemsInEachRow,
                ) {
                    val categories = state.recentlyItems.categories
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
                                scope?.launch {
                                    bottomSheetScaffoldState?.bottomSheetState?.expand()
                                }
                            }
                        )
                    }
                    repeat(maxItemsInEachRow - categories.size % maxItemsInEachRow) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        items(
            items = state.types,
            key = {
                it.typeIdTimestamp
            }
        ) {
            if (it.categories.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(24.dp).background(
                                color = Color(it.colorArgb),
                                shape = RoundedCornerShape(8.dp)
                            )
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.titleSmall
                        )

                    }
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        maxItemsInEachRow = maxItemsInEachRow,
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
                                    scope?.launch {
                                        bottomSheetScaffoldState?.bottomSheetState?.expand()
                                    }
                                }
                            )

                        }
                        repeat(maxItemsInEachRow - it.categories.size % maxItemsInEachRow) {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                    }

                }

            }
        }
    }

}