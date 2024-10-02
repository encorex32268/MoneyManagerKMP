@file:OptIn(KoinExperimentalAPI::class, KoinExperimentalAPI::class, KoinExperimentalAPI::class)

package feature.home.add.type

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import feature.core.domain.model.Type
import feature.core.presentation.Texts
import feature.core.presentation.components.TwoButtonDialog
import feature.core.presentation.date.DateConverter
import feature.core.presentation.reorderable.ReorderableItem
import feature.core.presentation.reorderable.detectReorderAfterLongPress
import feature.core.presentation.reorderable.rememberReorderableLazyListState
import feature.core.presentation.reorderable.reorderable
import feature.home.add.type.components.ColorPickerDialog
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.flow.collectLatest
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_content
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun TypesScreenRoot(
    viewModel: TypeViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    navigateToTypeCategoryEdit: (TypeUi) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(viewModel){
        viewModel.uiEvent.collectLatest {
            onBack()
        }
    }
    TypesScreen(
        items = state.items,
        onEvent = viewModel::onEvent,
        navigateToTypeCategoryEdit = navigateToTypeCategoryEdit
    )

}


@Composable
fun TypesScreen(
    items: List<TypeUi> = emptyList(),
    onEvent: (TypeEvent) -> Unit ={},
    navigateToTypeCategoryEdit: (TypeUi) -> Unit = {}
) {
    var isShowDeleteDialog by remember {
        mutableStateOf(false)
    }
    var isShowColorPicker by remember {
        mutableStateOf(false)
    }
    var currentType by remember {
        mutableStateOf<TypeUi?>(null)
    }

    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            onEvent(
                TypeEvent.OnItemMove(
                    from,to
                )
            )
        }
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                onClick = {
                    onEvent(
                        TypeEvent.OnBackClick
                    )
                }
            ){
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {
                    isShowColorPicker = true
                },
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ){
            LazyColumn(
                state = state.listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .reorderable(state)
                    .detectReorderAfterLongPress(state),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = items,
                    key = {
                        it.typeIdTimestamp
                    }
                ) { item ->
                    ReorderableItem(
                        modifier = Modifier.padding(4.dp),
                        reorderableState = state,
                        key = item.typeIdTimestamp
                    ) { isDragging ->
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface
                                )
                            ,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = Color.LightGray
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(item.colorArgb),
                                        shape = CircleShape
                                    )
                                    .size(24.dp)
                            )
                            Texts.TitleMedium(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp)
                                ,
                                text = item.name
                            )
                            IconButton(
                                onClick = {
                                    navigateToTypeCategoryEdit(item)
                                }
                            ){
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            }
                            IconButton(
                                onClick = {
                                    currentType = item
                                    isShowDeleteDialog = true
                                }
                            ){
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }

                        }
                    }
                }
            }


        }


    }

    if (isShowDeleteDialog){
        TwoButtonDialog(
            title = stringResource(Res.string.dialog_delete_title),
            content = stringResource(Res.string.dialog_delete_content),
            onConfirmButtonClick = {
                currentType?.let{
                    onEvent(
                        TypeEvent.OnHide(it)
                    )
                }
            },
            onDismissRequest = {
                isShowDeleteDialog = false
                currentType = null
            }
        )
    }
    if (isShowColorPicker){
        ColorPickerDialog(
            onDismissRequest = {
                currentType = null
                isShowColorPicker = false
            },
            onDone = { colorArgb , name ->
                if (currentType == null){
                    onEvent(
                        TypeEvent.OnNew(
                            TypeUi(
                                name = name,
                                colorArgb = colorArgb,
                                order = items.size,
                                typeIdTimestamp = DateConverter.getNowDateTimestamp()
                            )
                        )
                    )
                }
            },
            currentColor = currentType?.colorArgb?:0,
            name = currentType?.name?:""
        )
    }

}
