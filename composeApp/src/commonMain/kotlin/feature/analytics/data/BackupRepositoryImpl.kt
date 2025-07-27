package feature.analytics.data

import AppLogger
import core.domain.model.Category
import core.domain.model.Expense
import core.domain.model.SpendingLimit
import core.domain.model.Type
import core.domain.repository.ExpenseRepository
import core.domain.repository.SpendingLimitRepository
import core.domain.repository.TypeRepository
import core.presentation.date.DateConverter
import core.presentation.date.toStringDateYMDByTimestamp
import core.presentation.date.toTimestamp
import de.halfbit.csv.Csv
import de.halfbit.csv.buildCsv
import de.halfbit.csv.parseCsv
import feature.analytics.domain.BackupData
import feature.analytics.domain.BackupRepository
import feature.analytics.domain.BackupResult
import feature.analytics.domain.util.backup.BackupError
import getDownloadsPath
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class BackupRepositoryImpl(
    private val expenseRepository: ExpenseRepository,
    private val typeRepository: TypeRepository,
    private val spendingLimitRepository: SpendingLimitRepository,
    private val coroutineScope: CoroutineScope
) : BackupRepository {

    companion object {
        private const val NO = "No"
        private const val TYPE = "Type"
        private const val CATEGORY = "Category"
        private const val DESCRIPTION = "Description"
        private const val COST = "Cost"
        private const val DATE = "Date"
        private const val IS_INCOME = "IsIncome"
        private const val CONTENT = "Content"
        private const val OBJ_ID = "Object_id"

        private const val TYPE_NO = "Type_No"
        private const val TYPE_DATE = "Type_Date"
        private const val TYPE_NAME = "Type_Name"
        private const val TYPE_COLOR_ARGB = "Type_color_argb"
        private const val TYPE_ORDER = "Type_order"
        private const val TYPE_IS_SHOW = "Type_is_show"
        private const val TYPE_CATEGORIES = "Type_categories"
        private const val TYPE_OBJ_ID = "Type_id"

        private const val SPENDING_LIMIT_ID = "ID"
        private const val SPENDING_LIMIT_YEAR = "Year"
        private const val SPENDING_LIMIT_MONTH = "Month"
        private const val SPENDING_LIMIT_LIMIT = "Limit"

    }

    override suspend fun backup(): BackupResult {
        val nowTimestamp = DateConverter.getNowDateTimestamp()
        val downloadsPath = getDownloadsPath()
        if (downloadsPath.isBlank()) {
            return BackupResult(
                error = BackupError.LocalError.DOWNLOADS_PATH_IS_EMPTY
            )
        }
        val nowDate = DateConverter.getNowDate()
        val monthTimestampLongRange = DateConverter.getMonthStartAndEndTime(
            year = nowDate.year,
            month = nowDate.monthNumber
        )

        val expensesDeferred = coroutineScope.async {
            expenseRepository.getExpenseByTime(
                startTimeOfMonth = monthTimestampLongRange.first,
                endTimeOfMonth = monthTimestampLongRange.second
            )
        }

        val types = coroutineScope.async { typeRepository.getTypes() }
        val spendingLimit = coroutineScope.async { spendingLimitRepository.getAll() }
        AppLogger.d("BackupRepository","SpendingLimit: ${spendingLimit.await().firstOrNull()}")
        val csv = buildCsv(
            expensesDeferred.await().firstOrNull() ?: emptyList(),
            types.await().firstOrNull() ?: emptyList(),
            spendingLimit.await().firstOrNull()?:emptyList()
        )

        FileKit.saveFile(
            baseName = "MoneyManager_${nowTimestamp}",
            extension = "txt",
            initialDirectory = downloadsPath,
            bytes = csv.toCsvText().encodeToByteArray()
        )

        return BackupResult()

    }


    override suspend fun restoreData(platformFile: PlatformFile): BackupResult {
        var result = BackupResult()
        val jsonString = withContext(Dispatchers.IO) {
            platformFile.readBytes().decodeToString()
        }
        val csv = parseCsv(jsonString)
        if (csv.data.isEmpty()) {
            return BackupResult(
                error = BackupError.LocalError.CSV_DATA_IE_EMPTY
            )
        }
        val backupData = parse(csv)

        AppLogger.d("BackupRepository", " Restore ${backupData.expenses}")
        AppLogger.d("BackupRepository", " Restore ${backupData.types}")
        AppLogger.d("BackupRepository", " Restore ${backupData.spendingLimits}")

        val jobExpenseRestore = coroutineScope.async {
            try {
                expenseRepository.restore(backupData.expenses)
            } catch (e: Exception) {
                AppLogger.d("BackupRepository", " Restore Expense Error ${e.message}")
                result = result.copy(
                    error = BackupError.LocalError.RESTORE_EXCEPTION
                )
            }
        }
        val jobTypeRestore = coroutineScope.async {
            try {
                typeRepository.restore(backupData.types)
            } catch (e: Exception) {
                AppLogger.d("BackupRepository", " Restore Type Error ${e.message}")
                result = result.copy(
                    error = BackupError.LocalError.RESTORE_EXCEPTION
                )
            }
        }

        val jobSpendingLimitRestore = coroutineScope.async {
            try {
                spendingLimitRepository.restore(backupData.spendingLimits)
            } catch (e: Exception) {
                AppLogger.d("BackupRepository", " Restore Spending Error ${e.message}")
                result = result.copy(
                    error = BackupError.LocalError.RESTORE_EXCEPTION
                )
            }
        }

        awaitAll(
            jobExpenseRestore,
            jobTypeRestore,
            jobSpendingLimitRestore
        )

        return result
    }

    private fun parse(csv: Csv): BackupData {
        val types = mutableListOf<Type>()
        val expenses = mutableListOf<Expense>()
        val spendingLimits = mutableListOf<SpendingLimit>()


        csv.data.mapIndexed { index,dataRow ->
            AppLogger.d("BackupRepository Parse", "$dataRow")
            //如果有No 就等於先增 Expense
            val isUpsertExpense = !dataRow.value(NO).isNullOrBlank()
            val isType = !dataRow.value(TYPE_NO).isNullOrEmpty()
            val isSpending = !dataRow.value(SPENDING_LIMIT_ID).isNullOrEmpty()

            //先新增 Expense
            //類別ID TYPE -> 1752673560278
            //如果之前版本有 10010 ? -> 如果有的話 給他新的 Time
            val typeId = dataRow.value(TYPE)?.toLongOrNull()?:0

            //Icon Id -> 一個id 對應 一個 icon ex: 10012, food Icon
            val categoryId = dataRow.value(CATEGORY)?.toIntOrNull() ?: 0
            val description = dataRow.value(DESCRIPTION) ?: ""
            val cost = dataRow.value(COST)?.toLongOrNull() ?: 0
            val content = dataRow.value(CONTENT) ?: ""
            val idString = dataRow.value(OBJ_ID) ?: ""
            val date = dataRow.value(DATE)?.substringBefore("&")?.toLongOrNull() ?: 0L

            AppLogger.d("BackupRepository Parse","isSpending: $isSpending")
             when {
                isUpsertExpense && typeId != 0L-> {
                    val expense = Expense(
                        idString = idString,
                        categoryId = categoryId,
                        typeId = typeId,
                        description = description,
                        isIncome = false,
                        content = content,
                        timestamp = date,
                        cost = cost
                    )
                    AppLogger.d("BackupRepository Restore", "Expense:${expense}")
                    expenses.add(expense)
                }

                 isType -> {
                    //1752673560278
                    val typeDate = dataRow.value(TYPE_DATE)?.substringBefore("&")
                    val typeDateTimestamp = typeDate?.toLongOrNull() ?: (DateConverter.getNowDate()
                        .toTimestamp() + index + 1)
                    val typeName = dataRow.value(TYPE_NAME) ?: ""
                    val typeColorArgb = dataRow.value(TYPE_COLOR_ARGB)?.toIntOrNull() ?: 0
                    val typeOrder = dataRow.value(TYPE_ORDER)
                    val typeIdHex = dataRow.value(TYPE_OBJ_ID) ?: ""
                    val typeCategory = runCatching {
                        val json = dataRow.value(TYPE_CATEGORIES).orEmpty()
                        if (json.isBlank()) emptyList() else Json.decodeFromString<List<Category>>(
                            json
                        ).map {
                            it.copy(
                                typeId = typeDateTimestamp
                            )
                        }
                    }.getOrElse { exception ->
                        exception.printStackTrace()
                        emptyList()
                    }
                    if (typeIdHex.isNotEmpty() && typeOrder != null && typeDate?.isNotEmpty() == true) {
                        val type = Type(
                            typeIdHex = typeIdHex,
                            typeIdTimestamp = typeDateTimestamp,
                            name = typeName,
                            colorArgb = typeColorArgb,
                            order = typeOrder.toIntOrNull() ?: 0,
                            isShow = true,
                            categories = typeCategory
                        )
                        types.add(type)
                        AppLogger.d("BackRepository Restore", "Types:${type}")
                    }
                }

                isSpending -> {
                    AppLogger.d("BackupRepository Parse","SpendingLimit: isSpending ${isSpending}")
                    val year = dataRow.value(SPENDING_LIMIT_YEAR)?.toIntOrNull()?:0
                    val month = dataRow.value(SPENDING_LIMIT_MONTH)?.toIntOrNull()?:0
                    val limit = dataRow.value(SPENDING_LIMIT_LIMIT)?.toLongOrNull()?:0
                    AppLogger.d("BackupRepository Parse","SpendingLimit: $year/$month/$limit")
                    val canRestore = year != 0 && month != 0 && limit != 0L
                    if (canRestore){
                        val spendingLimit = SpendingLimit(
                            year = year,
                            month = month,
                            limit = limit
                        )
                        spendingLimits.add(spendingLimit)
                        AppLogger.d("BackRepository Restore", "Spending:${spendingLimits}")
                    }
                }
            }
        }

        return BackupData(
            expenses = expenses,
            types = types,
            spendingLimits = spendingLimits
        )
    }

    private fun buildCsv(
        expenses: List<Expense>,
        types: List<Type>,
        spendingLimit: List<SpendingLimit>
    ): Csv {
        val csv = buildCsv {
            row {
                value(NO)
                value(TYPE)
                value(CATEGORY)
                value(DESCRIPTION)
                value(COST)
                value(DATE)
                value(IS_INCOME)
                value(CONTENT)
                value(OBJ_ID)
                value(TYPE_NO)
                value(TYPE_DATE)
                value(TYPE_NAME)
                value(TYPE_COLOR_ARGB)
                value(TYPE_ORDER)
                value(TYPE_IS_SHOW)
                value(TYPE_CATEGORIES)
                value(TYPE_OBJ_ID)
                value(SPENDING_LIMIT_ID)
                value(SPENDING_LIMIT_YEAR)
                value(SPENDING_LIMIT_MONTH)
                value(SPENDING_LIMIT_LIMIT)
            }
            expenses.sortedBy { it.timestamp }.forEachIndexed { index, expense ->
                row {
                    value("$index")
                    value("${expense.typeId}")
                    value("${expense.categoryId}")
                    value(expense.description)
                    value("${expense.cost}")
                    value("${expense.timestamp}&${expense.timestamp.toStringDateYMDByTimestamp()}")
                    value("${expense.isIncome}")
                    value(expense.content)
                    value(expense.idString)
                }
            }
            types.sortedBy { it.order }.forEachIndexed { index, type ->
                row {
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("$index")
                    value("${type.typeIdTimestamp}&${type.typeIdTimestamp.toStringDateYMDByTimestamp()}")
                    value(type.name)
                    value("${type.colorArgb}")
                    value("${type.order}")
                    value("${type.isShow}")
                    value(Json.encodeToString(type.categories))
                    value(type.typeIdHex)
                }
            }
            spendingLimit.forEachIndexed { index , limit ->
                row {
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value("")
                    value(limit.id.toHexString())
                    value("${limit.year}")
                    value("${limit.month}")
                    value("${limit.limit}")
                }
            }

        }
        return csv
    }


}