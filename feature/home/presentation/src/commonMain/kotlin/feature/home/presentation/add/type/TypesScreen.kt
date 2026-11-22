@file:OptIn(KoinExperimentalAPI::class, KoinExperimentalAPI::class, KoinExperimentalAPI::class,
    ExperimentalMaterial3Api::class
)

package feature.home.presentation.add.type

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.presentation.components.DeleteDialog
import core.presentation.date.DateConverter
import feature.home.presentation.add.type.components.ColorPickerDialog
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_visibility_24
import moneymanagerkmp.composeapp.generated.resources.baseline_visibility_off_24
import moneymanagerkmp.composeapp.generated.resources.expense_detail
import moneymanagerkmp.composeapp.generated.resources.home_add_type_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun TypesScreenRoot(
    viewModel: TypeViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    navigateToTypeCategoryEdit: (TypeUi) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TypesScreen(
        state = state,
        onAction = {
            when(it){
                TypeAction.OnBackClick -> onBack()
                else -> Unit
            }
            viewModel.onAction(it)
        },
        navigateToTypeCategoryEdit = navigateToTypeCategoryEdit,
    )

}


@Composable
fun TypesScreen(
    state: TypesState,
    onAction: (TypeAction) -> Unit ={},
    navigateToTypeCategoryEdit: (TypeUi) -> Unit = {}
) {
    var isShowColorPicker by remember {
        mutableStateOf(false)
    }
    var currentType by remember {
        mutableStateOf<TypeUi?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        text = stringResource(Res.string.home_add_type_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(TypeAction.OnBackClick)
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
                            isShowColorPicker = true
                        },
                    ){
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = state.items,
                    key = { _, item -> item.typeIdHex }
                ) { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            enabled = index > 0,
                            onClick = {
                                onAction(TypeAction.OnMoveUp(item))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.padding(4.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(item.colorArgb),
                                    shape = CircleShape
                                )
                                .size(24.dp)
                        )
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = {
                                navigateToTypeCategoryEdit(item)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = {
                                onAction(
                                    TypeAction.OnHide(item)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.baseline_visibility_24),
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (state.itemsNotShowing.isNotEmpty()){
                            Modifier.weight(1f)
                        }else{
                            Modifier
                        }
                    )
                    .padding(horizontal = 8.dp)
            ){
                items(
                    items = state.itemsNotShowing
                ){
                    OnHideTypeItem(
                        modifier = Modifier.padding(4.dp),
                        item = it,
                        onDelete = {
                            onAction(
                                TypeAction.OnDelete(it)
                            )
                        },
                        onShow = {
                            onAction(
                                TypeAction.OnShow(it)
                            )
                        }
                    )
                }
            }

        }
    }
    if (state.isSaving){
        Box(
            modifier = Modifier.fillMaxSize() ,
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }
    if (isShowColorPicker){
        ColorPickerDialog(
            onDismissRequest = {
                currentType = null
                isShowColorPicker = false
            },
            onDone = { colorArgb , name ->
                if (currentType == null){
                    onAction(
                        TypeAction.OnNew(
                            TypeUi(
                                name = name,
                                colorArgb = colorArgb,
                                order = state.items.size,
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

@Composable
private fun OnHideTypeItem(
    modifier: Modifier = Modifier,
    item: TypeUi,
    onDelete: (TypeUi) -> Unit = {},
    onShow: (TypeUi) -> Unit = {},
){
    var isShowDeleteDialog by remember {
        mutableStateOf(false)
    }

    Row (
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            modifier = Modifier.alpha(0f),
            enabled = false,
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Box(
            modifier = Modifier
                .background(
                    color = Color(item.colorArgb),
                    shape = CircleShape
                )
                .size(24.dp)
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
            ,
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            textDecoration = TextDecoration.LineThrough

        )
        IconButton(
            onClick = {
                onShow(item)
            }
        ){
            Icon(
                imageVector = vectorResource(Res.drawable.baseline_visibility_off_24),
                contentDescription = null
            )
        }
        IconButton(
            onClick = {
                isShowDeleteDialog = true
            }
        ){
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }

    }
    if (isShowDeleteDialog){
        DeleteDialog(
            onConfirmButtonClick = {
                onDelete(item)
            },
            onDismissRequest = {
                isShowDeleteDialog = false
            }
        )
    }
}

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun TypesScreenPreview() {
    app.presentation.AppTheme {
        TypesScreen(
            state = TypesState()
        )
    }
}
