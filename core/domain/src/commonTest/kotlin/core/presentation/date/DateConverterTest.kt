package core.presentation.date

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DateConverterTest {

    @Test
    fun `getMonthStartAndEndTime handles leap year February 29`() {
        val (start, end) = DateConverter.getMonthStartAndEndTime(year = 2024, month = 2)
        val startDt = start.toLocalDateTime()
        val endDt = end.toLocalDateTime()

        assertEquals(2024, startDt.year)
        assertEquals(2, startDt.monthNumber)
        assertEquals(1, startDt.dayOfMonth)
        assertEquals(0, startDt.hour)
        assertEquals(0, startDt.minute)
        assertEquals(0, startDt.second)

        assertEquals(2024, endDt.year)
        assertEquals(2, endDt.monthNumber)
        assertEquals(29, endDt.dayOfMonth)
        assertEquals(23, endDt.hour)
        assertEquals(59, endDt.minute)
        assertEquals(59, endDt.second)
        assertTrue(start < end)
    }

    @Test
    fun `getMonthStartAndEndTime handles non-leap year February 28`() {
        val (_, end) = DateConverter.getMonthStartAndEndTime(year = 2025, month = 2)
        val endDt = end.toLocalDateTime()

        assertEquals(2025, endDt.year)
        assertEquals(2, endDt.monthNumber)
        assertEquals(28, endDt.dayOfMonth)
    }

    @Test
    fun `getMonthStartAndEndTime handles century non-leap year 1900 and leap year 2000`() {
        val (_, end1900) = DateConverter.getMonthStartAndEndTime(year = 1900, month = 2)
        val (_, end2000) = DateConverter.getMonthStartAndEndTime(year = 2000, month = 2)

        assertEquals(28, end1900.toLocalDateTime().dayOfMonth)
        assertEquals(29, end2000.toLocalDateTime().dayOfMonth)
    }

    @Test
    fun `getMonthStartAndEndTime handles 30-day and 31-day months including December`() {
        val (_, endApr) = DateConverter.getMonthStartAndEndTime(year = 2024, month = 4)
        val (_, endDec) = DateConverter.getMonthStartAndEndTime(year = 2024, month = 12)

        assertEquals(30, endApr.toLocalDateTime().dayOfMonth)
        assertEquals(31, endDec.toLocalDateTime().dayOfMonth)
        assertEquals(12, endDec.toLocalDateTime().monthNumber)
    }

    @Test
    fun `getMonthStartAndEndTime throws on out-of-range month`() {
        assertFailsWith<IllegalArgumentException> {
            DateConverter.getMonthStartAndEndTime(year = 2024, month = 0)
        }
        assertFailsWith<IllegalArgumentException> {
            DateConverter.getMonthStartAndEndTime(year = 2024, month = 13)
        }
    }

    @Test
    fun `toTimestamp returns 0L for null and round-trips non-null LocalDateTime`() {
        val nullDateTime: LocalDateTime? = null
        assertEquals(0L, nullDateTime.toTimestamp())

        val dt = LocalDateTime(2025, 7, 15, 14, 30, 45, 0)
        val ts = dt.toTimestamp()
        val roundTripped = ts.toLocalDateTime()

        assertEquals(2025, roundTripped.year)
        assertEquals(7, roundTripped.monthNumber)
        assertEquals(15, roundTripped.dayOfMonth)
        assertEquals(14, roundTripped.hour)
        assertEquals(30, roundTripped.minute)
        assertEquals(45, roundTripped.second)
    }

    @Test
    fun `LocalDate toEpochMilliseconds represents start of day`() {
        val date = LocalDate(2025, 3, 9)
        val dt = date.toEpochMilliseconds().toLocalDateTime()

        assertEquals(2025, dt.year)
        assertEquals(3, dt.monthNumber)
        assertEquals(9, dt.dayOfMonth)
        assertEquals(0, dt.hour)
        assertEquals(0, dt.minute)
        assertEquals(0, dt.second)
    }

    @Test
    fun `timestamp formatting helpers produce expected strings`() {
        val dt = LocalDateTime(2025, 11, 5, 9, 0, 0, 0)
        val ts = dt.toTimestamp()

        assertEquals("5 ${dt.dayOfWeek}", ts.toDayString())
        assertEquals(" 2025/11/5", ts.toStringDateYMDByTimestamp())
        assertEquals("11/5", ts.toStringDateMDByTimestamp())
        assertEquals("11/5 \n 2025", ts.toStringDateByTimestamp())
        assertEquals("11 \n 2025", ts.toStringDateMByTimestamp())
    }
}
