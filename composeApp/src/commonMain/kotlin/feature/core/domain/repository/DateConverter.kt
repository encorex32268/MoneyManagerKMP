package feature.core.domain.repository

interface DateConverter {
    fun getMonthStartAndEndTime(): Pair<Long,Long>
}