package feature.analytics.presentation.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.model.Category
import core.domain.model.Expense
import core.domain.model.Type
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
import core.presentation.date.DateConverter
import core.presentation.date.toStringDateYMDByTimestamp
import de.halfbit.csv.Csv
import de.halfbit.csv.buildCsv
import de.halfbit.csv.parseCsv
import feature.analytics.domain.util.backup.BackupError
import getDownloadsPath
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BackupViewModel(
    private val expenseRepository: ExpenseRepository,
    private val typeRepository: TypeRepository
) : ViewModel() {

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

    }

    private val _uiEvent = Channel<BackupUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: BackupEvent) {
        when (event) {
            BackupEvent.OnBackup -> backup()
            is BackupEvent.OnImport -> {
                event.platformFile?.let {
                    restoreData(it)
                }
            }

            else -> Unit
        }
    }


    private fun backup() {
        viewModelScope.launch(Dispatchers.Main) {
            val nowTimestamp = DateConverter.getNowDateTimestamp()
            val downloadsPath = getDownloadsPath()
            if (downloadsPath.isBlank()) {
                _uiEvent.send(
                    BackupUiEvent.SendError(
                        error = BackupError.Error.DOWNLOADS_PATH_IS_EMPTY
                    )
                )
                return@launch
            }
            val nowDate = DateConverter.getNowDate()
            val monthTimestampLongRange = DateConverter.getMonthStartAndEndTime(
                year = nowDate.year,
                month = nowDate.monthNumber
            )
            val expensesDeferred = expenseRepository.getExpenseByTime(
                startTimeOfMonth = monthTimestampLongRange.first,
                endTimeOfMonth = monthTimestampLongRange.second
            )

            val types = async { typeRepository.getTypes() }.await()

            combine(expensesDeferred, types) { expenses, types ->

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
                }

                FileKit.saveFile(
                    baseName = "MoneyManager_${nowTimestamp}",
                    extension = "txt",
                    initialDirectory = downloadsPath,
                    bytes = csv.toCsvText().encodeToByteArray()
                )

                _uiEvent.send(
                    BackupUiEvent.TaskSuccess
                )

            }.launchIn(this)

        }
    }

    private fun restoreData(
        platformFile: PlatformFile
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonString = platformFile.readBytes().decodeToString()
            val csv = parseCsv(jsonString)
            if (csv.data.isEmpty()){
                _uiEvent.send(
                    BackupUiEvent.SendError(
                        error = BackupError.Error.CSV_DATA_IE_EMPTY
                    )
                )
                return@launch
            }
            val expenses = parseExpenses(csv)
            val types = parseTypes(csv)

            val jobExpenseRestore = async {
                try {
                    expenseRepository.restore(expenses)
                }catch (e: Exception){
                    _uiEvent.send(
                        BackupUiEvent.SendError(
                            error = BackupError.Error.RESTORE_EXCEPTION
                        )
                    )
                    return@async
                }
            }
            val jobTypeRestore = async {
                try {
                    typeRepository.restore(types)
                }catch (e: Exception){
                    _uiEvent.send(
                        BackupUiEvent.SendError(
                            error = BackupError.Error.RESTORE_EXCEPTION
                        )
                    )
                    return@async
                }
            }

            awaitAll(jobExpenseRestore,jobTypeRestore)
            _uiEvent.send(
                BackupUiEvent.TaskSuccess
            )

        }
    }

    private fun parseExpenses(csv: Csv): List<Expense>{
        return csv.data.map {
            val datRow = it
            val typeId = datRow.value(TYPE)?.toLongOrNull() ?: 0
            val categoryId = datRow.value(CATEGORY)?.toIntOrNull() ?: 0
            val description = datRow.value(DESCRIPTION) ?: ""
            val cost = datRow.value(COST)?.toLongOrNull() ?: 0
            val date = datRow.value(DATE)?.split("&")?.get(0)?.toLongOrNull() ?: 0
            val isIncomeString = datRow.value(IS_INCOME) ?: "false"
            val content = datRow.value(CONTENT) ?: ""
            val idString = datRow.value(OBJ_ID) ?: ""
            Expense(
                idString = idString,
                categoryId = categoryId,
                typeId = typeId,
                description = description,
                isIncome = isIncomeString == "true",
                content = content,
                timestamp = date,
                cost = cost
            )
        }
    }

    private fun parseTypes(csv: Csv) : List<Type> {
        val types = mutableListOf<Type>()

        csv.data.forEach { datRow ->
            val typeDate = datRow.value(TYPE_DATE)
            val typeName = datRow.value(TYPE_NAME)?:""
            val typeColorArgb = datRow.value(TYPE_COLOR_ARGB)?.toIntOrNull()?:0
            val typeOrder = datRow.value(TYPE_ORDER)
            val typeIsShow = datRow.value(TYPE_IS_SHOW) == "true"
            val typeCategory = runCatching {
                val json = datRow.value(TYPE_CATEGORIES).orEmpty()
                if (json.isBlank()) emptyList() else Json.decodeFromString<List<Category>>(json)
            }.getOrElse {
                viewModelScope.launch {
                    _uiEvent.send(
                        BackupUiEvent.SendError(
                            error = BackupError.Error.JSON_DECODE_ERROR
                        )
                    )
                }
                emptyList()
            }

            val typeIdHex = datRow.value(TYPE_OBJ_ID)?:""
            if (typeIdHex.isNotEmpty() && typeOrder != null && typeDate.isNullOrEmpty()){
                types.add(
                    Type(
                        typeIdHex = typeIdHex,
                        typeIdTimestamp = typeDate?.toLongOrNull()?:1,
                        name = typeName,
                        colorArgb = typeColorArgb,
                        order = typeOrder.toIntOrNull()?:1,
                        isShow = typeIsShow,
                        categories = typeCategory
                    )
                )
            }
        }
        return types
    }
}