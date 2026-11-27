@file:OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)

package feature.home.presentation.add.type

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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.HorizontalDivider
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
import core.presentation.components.MoneyCard
import core.presentation.date.DateConverter
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import feature.home.presentation.add.type.components.ColorPickerDialog
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_visibility_24
import moneymanagerkmp.composeapp.generated.resources.baseline_visibility_off_24
import moneymanagerkmp.composeapp.generated.resources.cd_add_expense
import moneymanagerkmp.composeapp.generated.resources.cd_back
import moneymanagerkmp.composeapp.generated.resources.cd_delete
import moneymanagerkmp.composeapp.generated.resources.cd_edit
import moneymanagerkmp.composeapp.generated.resources.cd_move_up
import moneymanagerkmp.composeapp.generated.resources.cd_toggle_visibility
import moneymanagerkmp.composeapp.generated.resources.home_add_type_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun TypesScreenRoot(
    viewModel: TypeViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    navigateToTypeCategoryEdit: (TypeUi) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TypesScreen(
        state = state,
        onAction = {
            when (it) {
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
    onAction: (TypeAction) -> Unit = {},
    navigateToTypeCategoryEdit: (TypeUi) -> Unit = {},
) {
    var isShowColorPicker by remember { mutableStateOf(false) }
    var currentType by remember { mutableStateOf<TypeUi?>(null) }

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
                    Text(
                        text = stringResource(Res.string.home_add_type_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(TypeAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.cd_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isShowColorPicker = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(Res.string.cd_add_expense),
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = MoneyTokens.ScreenHorizontalPadding,
                vertical = 8.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            if (state.items.isNotEmpty()) {
                item(key = "visible_types_card") {
                    MoneyCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 4.dp),
                    ) {
                        state.items.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navigateToTypeCategoryEdit(item) }
                                    .padding(horizontal = 6.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(
                                    enabled = index > 0,
                                    onClick = { onAction(TypeAction.OnMoveUp(item)) },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = stringResource(Res.string.cd_move_up),
                                        tint = if (index > 0) MoneyTheme.colors.ink else MoneyTheme.colors.muted.copy(alpha = 0.25f),
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(
                                            color = Color(item.colorArgb),
                                            shape = CircleShape,
                                        )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MoneyTheme.colors.ink,
                                )
                                IconButton(onClick = { navigateToTypeCategoryEdit(item) }) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = stringResource(Res.string.cd_edit),
                                        tint = MoneyTheme.colors.muted,
                                    )
                                }
                                IconButton(onClick = { onAction(TypeAction.OnHide(item)) }) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = vectorResource(Res.drawable.baseline_visibility_24),
                                        contentDescription = stringResource(Res.string.cd_toggle_visibility),
                                        tint = MoneyTheme.colors.muted,
                                    )
                                }
                            }
                            if (index < state.items.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    thickness = 1.dp,
                                    color = MoneyTheme.colors.line,
                                )
                            }
                        }
                    }
                }
            }

            if (state.itemsNotShowing.isNotEmpty()) {
                item(key = "hidden_types_card") {
                    MoneyCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 4.dp),
                    ) {
                        state.itemsNotShowing.forEachIndexed { index, item ->
                            OnHideTypeItem(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                                item = item,
                                onDelete = { onAction(TypeAction.OnDelete(it)) },
                                onShow = { onAction(TypeAction.OnShow(it)) },
                            )
                            if (index < state.itemsNotShowing.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    thickness = 1.dp,
                                    color = MoneyTheme.colors.line,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.isSaving) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = MoneyTheme.colors.ink)
        }
    }

    if (isShowColorPicker) {
        ColorPickerDialog(
            onDismissRequest = {
                currentType = null
                isShowColorPicker = false
            },
            onDone = { colorArgb, name ->
                if (currentType == null) {
                    onAction(
                        TypeAction.OnNew(
                            TypeUi(
                                name = name,
                                colorArgb = colorArgb,
                                order = state.items.size,
                                typeIdTimestamp = DateConverter.getNowDateTimestamp(),
                            )
                        )
                    )
                }
            },
            currentColor = currentType?.colorArgb ?: MoneyTokens.MacaronPresets.first().value.toInt(),
            name = currentType?.name ?: "",
        )
    }
}

@Composable
private fun OnHideTypeItem(
    modifier: Modifier = Modifier,
    item: TypeUi,
    onDelete: (TypeUi) -> Unit = {},
    onShow: (TypeUi) -> Unit = {},
) {
    var isShowDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(0.55f),
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(
                    color = Color(item.colorArgb),
                    shape = CircleShape,
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = item.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MoneyTheme.colors.muted,
            textDecoration = TextDecoration.LineThrough,
        )
        IconButton(onClick = { onShow(item) }) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = vectorResource(Res.drawable.baseline_visibility_off_24),
                contentDescription = stringResource(Res.string.cd_toggle_visibility),
                tint = MoneyTheme.colors.muted,
            )
        }
        IconButton(onClick = { isShowDeleteDialog = true }) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(Res.string.cd_delete),
                tint = MoneyTheme.colors.budgetDanger,
            )
        }
    }

    if (isShowDeleteDialog) {
        DeleteDialog(
            onConfirmButtonClick = { onDelete(item) },
            onDismissRequest = { isShowDeleteDialog = false },
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun TypesScreenPreview() {
    app.presentation.AppTheme {
        TypesScreen(state = TypesState())
    }
}
