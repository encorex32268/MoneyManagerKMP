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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import feature.core.presentation.Texts
import feature.core.presentation.model.CategoryUi
import feature.core.presentation.reorderable.ReorderableItem
import feature.core.presentation.reorderable.detectReorderAfterLongPress
import feature.core.presentation.reorderable.rememberReorderableLazyGridState
import feature.core.presentation.reorderable.reorderable
import feature.home.presentation.add.components.CategoryItem
import feature.home.presentation.add.type.TypeUi
import feature.home.presentation.add.type.category.components.CategoryAddDialog
import feature.home.presentation.add.type.components.ColorPickerDialog
import feature.core.presentation.noRippleClick
import kotlinx.coroutines.flow.collectLatest
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
    onBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel){
        viewModel.uiEvent.collectLatest {
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
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState)
        }
    ){
        TypeCategoryEditScreen(
            state = state,
            onEvent = viewModel::onEvent
        )

    }

}

@Composable
fun TypeCategoryEditScreen(
    state: TypeCategoryEditState,
    onEvent: (TypeCategoryEditEvent) -> Unit = {}
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onEvent(
                        TypeCategoryEditEvent.OnBackClick
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .background(Color.White)
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
                        .size(32.dp)
                )
                Texts.TitleMedium(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    text = state.typeUi.name
                )
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                ,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    state = reorderState.gridState,
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                        .border(
                            1.dp,
                            color = Color.Black,
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
                                .padding(8.dp)
                                .detectReorderAfterLongPress(reorderState)
                                .background(Color.White)
                            ,
                            reorderableState = reorderState,
                            key = item.uuid
                        ) { isDragging ->
                            Icon(
                                modifier = Modifier
                                    .size(12.dp)
                                    .align(Alignment.TopEnd)
                                    .noRippleClick {
                                        onEvent(
                                            TypeCategoryEditEvent.OnItemRemove(item)
                                        )
                                    }
                                ,
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                            CategoryItem(
                                categoryUi = item,
                                isClicked = isDragging
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
                            1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    columns = GridCells.Fixed(4),
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
