@file:OptIn(ExperimentalMaterial3Api::class)

package feature.home.presentation.add.type.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.presentation.CategoryList
import core.presentation.ObserveAsEvents
import core.presentation.components.MoneyCard
import core.presentation.model.CategoryUi
import core.presentation.navigation.NavigationLayoutType
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import feature.home.presentation.add.components.CategoryItem
import feature.home.presentation.add.type.TypeUi
import feature.home.presentation.add.type.category.components.CategoryAddDialog
import feature.home.presentation.add.type.components.ColorPickerDialog
import feature.home.presentation.reorderable.ReorderableItem
import feature.home.presentation.reorderable.detectReorderAfterLongPress
import feature.home.presentation.reorderable.rememberReorderableLazyGridState
import feature.home.presentation.reorderable.reorderable
import kotlinx.coroutines.launch
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.cd_back
import moneymanagerkmp.composeapp.generated.resources.cd_edit
import moneymanagerkmp.composeapp.generated.resources.cd_save
import moneymanagerkmp.composeapp.generated.resources.save_snackbar
import moneymanagerkmp.composeapp.generated.resources.type_edit_current_categories
import moneymanagerkmp.composeapp.generated.resources.type_edit_icon_library
import moneymanagerkmp.composeapp.generated.resources.type_edit_item_count
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun TypeCategoryEditScreenRoot(
    typeUi: TypeUi,
    viewModel: TypeCategoryEditViewModel = koinInject {
        parametersOf(typeUi)
    },
    onBack: () -> Unit = {},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.uiEvent, viewModel) {
        when (it) {
            TypeCategoryEditUiEvent.OnBack -> onBack()
            TypeCategoryEditUiEvent.OnSavedShow -> {
                scope.launch {
                    hostState.showSnackbar(
                        getString(Res.string.save_snackbar),
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }
    }
    Scaffold(
        containerColor = MoneyTheme.colors.bg,
        snackbarHost = { SnackbarHost(hostState) },
    ) {
        TypeCategoryEditScreen(
            state = state,
            onAction = viewModel::onAction,
            navigationLayoutType = navigationLayoutType,
        )
    }
}

@Composable
fun TypeCategoryEditScreen(
    state: TypeCategoryEditState,
    onAction: (TypeCategoryEditAction) -> Unit = {},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
) {
    var currentCategoryUi by remember { mutableStateOf<CategoryUi?>(null) }
    var isShowColorPicker by remember { mutableStateOf(false) }
    var isShowAddDialog by remember { mutableStateOf(false) }

    val reorderState = rememberReorderableLazyGridState(
        onMove = { from, to ->
            onAction(TypeCategoryEditAction.OnItemMove(from, to))
        }
    )
    val gridCells = remember(navigationLayoutType) {
        when (navigationLayoutType) {
            NavigationLayoutType.BOTTOM_NAVIGATION -> 4
            else -> 5
        }
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
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { isShowColorPicker = true }
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = Color(state.typeUi.colorArgb),
                                    shape = CircleShape,
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = state.typeUi.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MoneyTheme.colors.ink,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(Res.string.cd_edit),
                            tint = MoneyTheme.colors.muted,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(TypeCategoryEditAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.cd_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(TypeCategoryEditAction.OnSaveClick) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(Res.string.cd_save),
                            tint = MoneyTheme.colors.budgetSafe,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        ItemsSection(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MoneyTokens.ScreenHorizontalPadding, vertical = 8.dp),
            navigationLayoutType = navigationLayoutType,
            firstPane = { modifier ->
                MoneyCard(
                    modifier = modifier,
                    contentPadding = PaddingValues(14.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(Res.string.type_edit_current_categories),
                            style = MaterialTheme.typography.labelSmall,
                            color = MoneyTheme.colors.muted,
                        )
                        Text(
                            text = stringResource(
                                Res.string.type_edit_item_count,
                                state.typeUi.categories.size,
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = MoneyTheme.colors.muted,
                        )
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(gridCells),
                        state = reorderState.gridState,
                        contentPadding = PaddingValues(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .reorderable(reorderState),
                    ) {
                        items(
                            items = state.typeUi.categories,
                            key = { it.uuid },
                        ) { item ->
                            ReorderableItem(
                                modifier = Modifier.detectReorderAfterLongPress(reorderState),
                                reorderableState = reorderState,
                                key = item.uuid,
                            ) { isDragging ->
                                CategoryItem(
                                    categoryUi = item.copy(colorArgb = state.typeUi.colorArgb),
                                    isClicked = isDragging,
                                    isNeedDeleteIcon = true,
                                    onDeleteClick = {
                                        onAction(TypeCategoryEditAction.OnItemRemove(item))
                                    },
                                )
                            }
                        }
                    }
                }
            },
            secondPane = { modifier ->
                MoneyCard(
                    modifier = modifier,
                    contentPadding = PaddingValues(14.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                        text = stringResource(Res.string.type_edit_icon_library),
                        style = MaterialTheme.typography.labelSmall,
                        color = MoneyTheme.colors.muted,
                    )
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        columns = GridCells.Fixed(gridCells),
                        contentPadding = PaddingValues(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(
                            items = state.categories,
                            key = { it.id },
                        ) { item ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MoneyTheme.colors.bg)
                                    .clickable {
                                        currentCategoryUi = item
                                        isShowAddDialog = true
                                    }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = CategoryList.getCategoryIconById(item.id.toLong()),
                                    contentDescription = item.name,
                                    tint = MoneyTheme.colors.ink,
                                )
                            }
                        }
                    }
                }
            },
        )
    }

    if (isShowAddDialog && currentCategoryUi != null) {
        CategoryAddDialog(
            categoryUi = currentCategoryUi!!,
            groupColorArgb = state.typeUi.colorArgb,
            onDismissRequest = { isShowAddDialog = false },
            onDone = {
                onAction(TypeCategoryEditAction.OnItemAdd(categoryUi = it))
            },
        )
    }
    if (isShowColorPicker) {
        ColorPickerDialog(
            onDismissRequest = { isShowColorPicker = false },
            onDone = { colorArgb, name ->
                onAction(
                    TypeCategoryEditAction.OnTypeEdit(
                        name = name,
                        colorArgb = colorArgb,
                    )
                )
            },
            currentColor = state.typeUi.colorArgb,
            name = state.typeUi.name,
        )
    }
}

@Composable
private fun ItemsSection(
    modifier: Modifier = Modifier,
    navigationLayoutType: NavigationLayoutType,
    firstPane: @Composable (Modifier) -> Unit,
    secondPane: @Composable (Modifier) -> Unit,
) {
    when (navigationLayoutType) {
        NavigationLayoutType.BOTTOM_NAVIGATION -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                firstPane(Modifier.fillMaxWidth().weight(1f))
                secondPane(Modifier.fillMaxWidth().weight(1f))
            }
        }
        else -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                firstPane(Modifier.weight(1f))
                secondPane(Modifier.weight(1f))
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun TypeCategoryEditScreenPreview() {
    app.presentation.AppTheme {
        TypeCategoryEditScreen(
            state = TypeCategoryEditState(
                typeUi = TypeUi(
                    typeIdTimestamp = 0L,
                    name = "Food",
                    colorArgb = -1710479,
                    order = 0,
                )
            )
        )
    }
}
