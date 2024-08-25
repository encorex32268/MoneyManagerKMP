@file:OptIn(ExperimentalResourceApi::class)

package feature.home

import org.jetbrains.compose.resources.ExperimentalResourceApi

//@ExperimentalResourceApi
//object HomeTab : CustomTab {
//
//    @Composable
//    override fun Content() {
//        val navigator = LocalNavigator.currentOrThrow
//        val navigatorTab = LocalTabNavigator.current
//        val homeScreenModel = getScreenModel<HomeScreenModel>()
//        val state by homeScreenModel.state.collectAsState()
//        LaunchedEffect(Unit){
//            homeScreenModel.onEvent(
//                HomeEvent.OnDatePick(
//                    isInit = true
//                )
//            )
//        }
//        Scaffold(
//            containerColor = Color.White,
//            modifier = Modifier.fillMaxSize(),
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = {
//                        navigator.parent?.push(AddScreen(null))
//                    },
//                    containerColor = MaterialTheme.colorScheme.onBackground
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription =null,
//                        tint = MaterialTheme.colorScheme.background
//                    )
//                }
//            }
//        ){
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(it)
//            ) {
//                DatePicker(
//                    year = state.nowDateYear.toIntOrNull()?:0,
//                    month = state.nowDateMonth.toIntOrNull()?:0,
//                    onDateChange = { year , month ->
//                        homeScreenModel.onEvent(
//                            HomeEvent.OnDatePick(
//                                year = year,
//                                month = month
//                            )
//                        )
//                    }
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                AmountTextLayout(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .noRippleClick {
//                            navigatorTab.current = ChartTab
//                        }
//                    ,
//                    income = state.income,
//                    expense = state.expense,
//                    total = state.totalAmount
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ){
//                items(
//                    items = state.items,
//                    key = {
//                        it
//                    }
//                ) {(_ , expenses) ->
//                    ExpenseItem(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        items = expenses,
//                        onItemClick = {
//                            navigator.parent?.push(
//                                EditExpenseScreen(it)
//                            )
//                        }
//                    )
//                }
//                item {
//                    Spacer(modifier = Modifier.height(100.dp))
//                }
//            }
//            }
//        }
//    }
//
//    override val customTabOptions: CustomTabOptions
//        @Composable
//        get() {
//            val selectedIcon = painterResource(Res.drawable.baseline_receipt_24_filled)
//            val unSelectedIcon = painterResource(Res.drawable.baseline_receipt_24_outline)
//            return remember{
//                CustomTabOptions(
//                    index = 0u,
//                    title = "Home",
//                    selectedIcon = selectedIcon,
//                    unSelectedIcon = unSelectedIcon
//                )
//            }
//        }
//
//
//}