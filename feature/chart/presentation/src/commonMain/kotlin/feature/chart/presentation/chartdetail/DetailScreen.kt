@file:OptIn(
    ExperimentalResourceApi::class,
    KoinExperimentalAPI::class,
    ExperimentalMaterial3Api::class,
)

package feature.chart.presentation.chartdetail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.components.MoneyCard
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import feature.home.domain.mapper.toExpenseUi
import feature.home.presentation.components.ExpenseItem
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.cd_back
import moneymanagerkmp.composeapp.generated.resources.chart_group_total
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import toMoneyString

@Composable
fun DetailScreenRoot(
    items: List<Expense> = emptyList(),
    type: Type,
    viewModel: DetailViewModel = koinViewModel {
        parametersOf(items, type)
    },
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailScreen(
        state = state,
        onBack = onBack,
    )
}

@Composable
fun DetailScreen(
    state: DetailState,
    onBack: () -> Unit = {},
) {
    Scaffold(
        containerColor = MoneyTheme.colors.bg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MoneyTheme.colors.bg,
                    titleContentColor = MoneyTheme.colors.ink,
                    navigationIconContentColor = MoneyTheme.colors.ink,
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(13.dp)
                                .background(
                                    color = Color(state.type.colorArgb),
                                    shape = CircleShape,
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = state.type.name,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.cd_back),
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = MoneyTokens.ScreenHorizontalPadding, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MoneyCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(18.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = stringResource(Res.string.chart_group_total),
                        style = MaterialTheme.typography.titleSmall,
                        color = MoneyTheme.colors.muted,
                    )
                    Text(
                        text = state.total.toMoneyString(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                        ),
                        color = MoneyTheme.colors.ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            state.items.forEach { pair ->
                val expenses = remember(pair.second, state.type) {
                    pair.second.map { expense ->
                        expense.toExpenseUi().copy(
                            type = state.type,
                            colorRGB = state.type.colorArgb,
                        )
                    }
                }
                ExpenseItem(
                    modifier = Modifier.fillMaxWidth(),
                    items = expenses,
                    isClick = false,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun DetailScreenPreview() {
    app.presentation.AppTheme {
        DetailScreen(
            state = DetailState(
                total = 1500L,
                type = Type(
                    typeIdTimestamp = 0L,
                    name = "食",
                    colorArgb = 0xFFE2D94D.toInt(),
                    order = 0,
                ),
            )
        )
    }
}
