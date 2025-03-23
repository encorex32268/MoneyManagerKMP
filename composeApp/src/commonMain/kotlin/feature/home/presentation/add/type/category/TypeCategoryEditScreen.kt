@file:OptIn(ExperimentalMaterial3Api::class)

package feature.home.presentation.add.type.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.presentation.ObserveAsEvents
import core.presentation.model.CategoryUi
import core.presentation.navigation.NavigationLayoutType
import core.presentation.noRippleClick
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
import moneymanagerkmp.composeapp.generated.resources.save_snackbar
import org.jetbrains.compose.resources.getString
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun TypeCategoryEditScreenRoot(
    typeUi: TypeUi,
    viewModel: TypeCategoryEditViewModel = koinInject{
        parametersOf(typeUi)
    },
    onBack: () -> Unit = {},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
) {
    val state by viewModel.state.collectAsState()
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.uiEvent, viewModel){
        when(it){
            TypeCategoryEditUiEvent.OnBack      -> {
                onBack()
            }
            TypeCategoryEditUiEvent.OnSavedShow -> {
                scope.launch {
                    hostState.showSnackbar(
                        getString(Res.string.save_snackbar),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState)
        }
    ){
        TypeCategoryEditScreen(
            state = state,
            onEvent = viewModel::onEvent,
            navigationLayoutType = navigationLayoutType
        )

    }

}

@Composable
fun TypeCategoryEditScreen(
    state: TypeCategoryEditState,
    onEvent: (TypeCategoryEditEvent) -> Unit = {},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
) {

    var currentCategoryUi by remember {
        mutableStateOf<CategoryUi?>(null)
    }

    var isShowColorPicker by remember {
        mutableStateOf(false)
    }
    var isShowAddDialog by remember {
        mutableStateOf(false)
    }
    val reorderState = rememberReorderableLazyGridState(
        onMove = { from , to ->
            onEvent(
                TypeCategoryEditEvent.OnItemMove(
                    from , to
                )
            )
        }
    )
    val gridCells by remember(navigationLayoutType) {
        mutableStateOf(
            when(navigationLayoutType){
                NavigationLayoutType.BOTTOM_NAVIGATION -> 4
                else ->  5
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                            .noRippleClick {
                                isShowColorPicker = true
                            }
                        ,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(state.typeUi.colorArgb),
                                    shape = CircleShape
                                )
                                .size(24.dp)
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = state.typeUi.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(
                                TypeCategoryEditEvent.OnBackClick
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                            
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(
                                TypeCategoryEditEvent.OnSaveClick
                            )
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ItemsSection(
                navigationLayoutType = navigationLayoutType,
                content = {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(gridCells),
                        state = reorderState.gridState,
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp)
                            .border(
                                0.dp,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .reorderable(reorderState)
                    ) {
                        items(
                            items = state.typeUi.categories,
                            key = {
                                it.uuid
                            }
                        )
                        { item ->
                            ReorderableItem(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .detectReorderAfterLongPress(reorderState)
                                ,
                                reorderableState = reorderState,
                                key = item.uuid
                            ) { isDragging ->
                                CategoryItem(
                                    categoryUi = item,
                                    isClicked = isDragging,
                                    isNeedDeleteIcon = true,
                                    onDeleteClick = {
                                        onEvent(
                                            TypeCategoryEditEvent.OnItemRemove(item)
                                        )
                                    }
                                )
                            }

                        }
                    }
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp)
                            .border(
                                0.dp,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        columns = GridCells.Fixed(gridCells),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            state.categories,
                            key = {
                                it.id
                            }
                        ) { item ->
                            CategoryItem(
                                categoryUi = item,
                                isClicked = false,
                                onItemClick = {
                                    currentCategoryUi = item
                                    isShowAddDialog = true
                                },
                                isVisibilityText = false
                            )
                        }


                    }
                }
            )

        }
    }
    if (isShowAddDialog){
        CategoryAddDialog(
            categoryUi = currentCategoryUi!!,
            onDismissRequest = {
                isShowAddDialog = false
            },
            onDone = {
                onEvent(
                    TypeCategoryEditEvent.OnItemAdd(
                        categoryUi = it
                    )
                )
            }
        )
    }
    if (isShowColorPicker){
        ColorPickerDialog(
            onDismissRequest = {
                isShowColorPicker = false
            },
            onDone = { colorArgb , name ->
                onEvent(
                    TypeCategoryEditEvent.OnTypeEdit(
                        name = name,
                        colorArgb = colorArgb
                    )
                )
            },
            currentColor = state.typeUi.colorArgb,
            name = state.typeUi.name
        )
    }


}

@Composable
private fun ItemsSection(
    modifier: Modifier = Modifier,
    navigationLayoutType: NavigationLayoutType,
    content: @Composable () -> Unit = {}
){
    when(navigationLayoutType){
        NavigationLayoutType.BOTTOM_NAVIGATION -> {
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ){
                content()
            }
        }
        else -> {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                content()
            }
        }
    }
}

