@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class,
    KoinExperimentalAPI::class
)

package feature.home.presentation.add

import AdMobBannerController
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.domain.model.Expense
import core.presentation.CategoryList
import core.presentation.ObserveAsEvents
import core.presentation.navigation.NavigationLayoutType
import feature.home.presentation.add.components.CalculateLayout
import feature.home.presentation.add.components.CategoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_edit_note_24
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.expense_detail
import moneymanagerkmp.composeapp.generated.resources.home_add_type_custom
import moneymanagerkmp.composeapp.generated.resources.home_add_type_default
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
    ObserveAsEvents(viewModel.uiEvent){
        when(it){
            AddUiEvent.Success -> {
                onGoBack()
            }
            else -> Unit
        }
    }
    when(navigationLayoutType){
        NavigationLayoutType.BOTTOM_NAVIGATION -> {
            AddScreen(
                state = state,
                onAction = { event ->
                    when(event){
                        AddAction.OnBack                  -> onGoBack()
                        AddAction.OnGoToCategoryEditClick -> onGoToCategoryEditClick()
                        else -> Unit
                    }
                    viewModel.onAction(event)
                }
            )
        }
        else -> {
            AddScreenNaviRail(
                state = state,
                onAction = { event ->
                    when(event){
                        AddAction.OnBack                  -> onGoBack()
                        AddAction.OnGoToCategoryEditClick -> onGoToCategoryEditClick()
                        else -> Unit
                    }
                    viewModel.onAction(event)
                }
            )
        }
    }
}


@Composable
fun AddScreen(
    state: AddState,
    onAction: (AddAction) -> Unit = {},
    isDebug: Boolean = false,
){
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val bottomSheetState = rememberStandardBottomSheetState(
        skipHiddenState = false,
        initialValue = SheetValue.Hidden
    )
    val bottomSheetScaffoldState  = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )
    val scope = rememberCoroutineScope()

    val shouldShowDefaultButtons = remember(state.recentlyItems?.categories, state.types) {
        state.recentlyItems?.categories.isNullOrEmpty() && state.types.isEmpty()
    }

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
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        text = stringResource(Res.string.expense),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(AddAction.OnBack)
                        }
                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onAction(AddAction.OnGoToCategoryEditClick)
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.baseline_edit_note_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
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
                onAction = onAction,
                month = state.monthNumber.toString(),
                day = state.dayOfMonth.toString(),
                state = state
            )
        }
    ){
        Column(
            modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)
        ) {
            if (shouldShowDefaultButtons && !state.isLoading){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp , Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(
                        onClick = {
                            onAction(AddAction.CreateDefaultType)
                        }
                    ){
                        Text(
                            text =  stringResource(Res.string.home_add_type_default),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Button(
                        onClick = {
                            onAction(AddAction.OnGoToCategoryEditClick)
                        }
                    ){
                        Text(
                            text = stringResource(Res.string.home_add_type_custom),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }else{
                ItemSection(
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp),
                    state = state,
                    onAction = onAction,
                    scope = scope,
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                )
            }
            if (!isDebug) {
                AdMobBannerController.AdMobBannerCompose(
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}



@Composable
fun AddScreenNaviRail(
    state: AddState,
    onAction: (AddAction) -> Unit = {}
){
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize()
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
                            onAction(AddAction.OnBack)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.expense),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(
                        onClick = {
                            onAction(AddAction.OnGoToCategoryEditClick)
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.baseline_edit_note_24),
                            contentDescription = null
                        )
                    }
                }
                if (state.recentlyItems?.categories.isNullOrEmpty() && state.types.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp , Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                onAction(AddAction.CreateDefaultType)
                            }
                        ){
                            Text(
                                text =  stringResource(Res.string.home_add_type_default),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Button(
                            onClick = {
                                onAction(AddAction.OnGoToCategoryEditClick)
                            }
                        ){
                            Text(
                                text = stringResource(Res.string.home_add_type_custom),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }else{
                    ItemSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(8.dp),
                        state = state,
                        onAction = onAction,
                        maxItemsInEachRow = 8
                    )

                }
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
                    onAction = onAction,
                    month = state.monthNumber.toString(),
                    day = state.dayOfMonth.toString(),
                    state = state,
                    navigationLayoutType = NavigationLayoutType.NAVIGATION_RAIL
                )

            }
        }


    }

}

private data class CategorySectionChip(
    val key: String,
    val label: String,
    val colorArgb: Int?,
)

@Composable
private fun ItemSection(
    modifier: Modifier = Modifier,
    state: AddState,
    onAction: (AddAction) -> Unit,
    scope: CoroutineScope? = null,
    bottomSheetScaffoldState: BottomSheetScaffoldState? = null,
    maxItemsInEachRow: Int = 4
) {
    val recentCategories = state.recentlyItems?.categories
    val recentlyTitle = stringResource(Res.string.recently)

    val sectionChips = remember(recentCategories, state.types, recentlyTitle) {
        buildList {
            if (!recentCategories.isNullOrEmpty()) {
                add(
                    CategorySectionChip(
                        key = "recently",
                        label = recentlyTitle,
                        colorArgb = null,
                    )
                )
            }
            state.types.forEach { typeUi ->
                if (typeUi.categories.isNotEmpty()) {
                    add(
                        CategorySectionChip(
                            key = typeUi.typeIdHex,
                            label = typeUi.name,
                            colorArgb = typeUi.colorArgb,
                        )
                    )
                }
            }
        }
    }

    var selectedSectionKey by rememberSaveable { mutableStateOf<String?>(null) }
    val activeSectionKey = remember(sectionChips, selectedSectionKey) {
        val current = selectedSectionKey
        if (current != null && sectionChips.any { it.key == current }) {
            current
        } else {
            sectionChips.firstOrNull()?.key
        }
    }

    Column(modifier = modifier) {
        if (sectionChips.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
            ) {
                items(
                    items = sectionChips,
                    key = { it.key },
                ) { chip ->
                    val selected = chip.key == activeSectionKey
                    FilterChip(
                        selected = selected,
                        onClick = {
                            selectedSectionKey = chip.key
                        },
                        label = {
                            Text(
                                text = chip.label,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        },
                        leadingIcon = chip.colorArgb?.let { argb ->
                            {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(
                                            color = Color(argb),
                                            shape = CircleShape,
                                        )
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        AnimatedContent(
            targetState = activeSectionKey,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            transitionSpec = {
                val initialIndex = sectionChips.indexOfFirst { it.key == initialState }
                val targetIndex = sectionChips.indexOfFirst { it.key == targetState }
                val isForward = targetIndex >= initialIndex

                (fadeIn(animationSpec = tween(180)) +
                    slideInHorizontally(animationSpec = tween(200)) { width ->
                        if (isForward) width / 6 else -width / 6
                    }) togetherWith
                    (fadeOut(animationSpec = tween(140)) +
                        slideOutHorizontally(animationSpec = tween(160)) { width ->
                            if (isForward) -width / 6 else width / 6
                        })
            },
            label = "CategorySectionTransition",
        ) { sectionKey ->
            val isRecently = sectionKey == "recently"
            val categories = remember(sectionKey, recentCategories, state.types) {
                when (sectionKey) {
                    "recently" -> recentCategories.orEmpty()
                    null -> emptyList()
                    else -> state.types.firstOrNull { it.typeIdHex == sectionKey }?.categories.orEmpty()
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(maxItemsInEachRow),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                itemsIndexed(
                    items = categories,
                    key = { index, categoryUi -> "${sectionKey}_${index}_${categoryUi.id}" },
                    contentType = { _, _ -> "category_item" }
                ) { _, categoryUi ->
                    val description = if (isRecently) {
                        categoryUi.name.ifEmpty {
                            CategoryList.getCategoryDescriptionById(categoryUi.id.toLong())
                        }
                    } else {
                        categoryUi.name
                    }
                    CategoryItem(
                        modifier = Modifier.fillMaxWidth(),
                        isClicked = categoryUi.isClick,
                        categoryUi = categoryUi,
                        onItemClick = {
                            onAction(
                                AddAction.OnItemSelected(
                                    categoryUi = categoryUi,
                                    description = description,
                                    isRecently = isRecently,
                                )
                            )
                            scope?.launch {
                                bottomSheetScaffoldState?.bottomSheetState?.expand()
                            }
                        }
                    )
                }
            }
        }
    }
}

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun AddScreenPreview() {
    app.presentation.AppTheme {
        AddScreen(
            state = AddState(isLoading = false),
            isDebug = true
        )
    }
}