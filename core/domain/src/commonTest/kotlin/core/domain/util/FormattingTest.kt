package core.domain.util

import core.domain.exts.formatAddZero
import kotlin.test.Test
import kotlin.test.assertEquals

class FormattingTest {

    @Test
    fun `zeroStringDisplay pads single digit non-negative numbers`() {
        assertEquals("00", zeroStringDisplay(0))
        assertEquals("09", zeroStringDisplay(9))
        assertEquals("10", zeroStringDisplay(10))
        assertEquals("100", zeroStringDisplay(100))
    }

    @Test
    fun `formatAddZero pads single digit numbers to width 2`() {
        assertEquals("00", 0.formatAddZero())
        assertEquals("05", 5.formatAddZero())
        assertEquals("10", 10.formatAddZero())
        assertEquals("123", 123.formatAddZero())
    }
}
