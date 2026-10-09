package com.leoaristocrat.semesta

import com.leoaristocrat.semesta.core.datastore.StudentFeesJson
import com.leoaristocrat.semesta.core.utils.CurrencyFormatter
import com.leoaristocrat.semesta.core.utils.GradingScaleUtils
import com.leoaristocrat.semesta.feature_expenses.domain.AttendanceRequirement
import com.leoaristocrat.semesta.feature_expenses.domain.StudentFee
import com.leoaristocrat.semesta.feature_user.domain.CurrencyPreference
import com.leoaristocrat.semesta.feature_user.domain.GradingScale
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [34])
class IndiaStudentSupportTest {
    @Test fun rupeesUseLakhsCroresAndDoNotChangeOtherCurrencies() {
        assertEquals("₹1,23,45,678 INR", CurrencyFormatter.format(12345678))
        assertEquals("₹500", CurrencyFormatter.format(500, includeCode = false))
        assertEquals("−₹2,14,74,83,648 INR", CurrencyFormatter.format(Int.MIN_VALUE))
        assertEquals("$50.000 COP", CurrencyFormatter.format(50000, CurrencyPreference.COP))
        assertEquals("₹3,00,00,00,000 INR", CurrencyFormatter.format(3_000_000_000L))
    }
    @Test fun tenPointScaleIsExplicitAndCustomScalesRemainAvailable() {
        assertEquals(10.0, GradingScaleUtils.maxGradeFor(GradingScale.ZERO_TO_TEN), 0.0)
        assertEquals(5.0, GradingScaleUtils.maxGradeFor(GradingScale.ZERO_TO_FIVE), 0.0)
        assertEquals(100.0, GradingScaleUtils.maxGradeFor(GradingScale.ZERO_TO_HUNDRED), 0.0)
    }
    @Test fun attendanceRecoveryAndBufferRespectTheBoundary() {
        assertEquals(12, AttendanceRequirement.classesNeeded(6, 6, 75))
        assertEquals(0, AttendanceRequirement.classesCanMiss(6, 6, 75))
        assertEquals(1, AttendanceRequirement.classesCanMiss(9, 2, 75))
        assertEquals(0, AttendanceRequirement.classesNeeded(9, 3, 75))
        assertNull(AttendanceRequirement.classesNeeded(9, 1, 100))
        assertEquals(0, AttendanceRequirement.classesNeeded(0, 0, 100))
    }
    @Test fun feeBackupRoundTripKeepsPartialPaymentsAndDueDates() {
        val today = LocalDate.of(2026, 10, 7)
        val fee = StudentFee("tuition", "Semester 1 tuition", 75000, 25000, today.minusDays(1).toEpochDay())
        assertEquals(listOf(fee), StudentFeesJson.decode(StudentFeesJson.encode(listOf(fee)).toString()))
        assertEquals(50000, fee.remaining)
        assertTrue(fee.overdue(today))
        assertFalse(fee.copy(paid = fee.amount).overdue(today))
        assertFalse(fee.copy(paid = fee.amount + 1).valid)
        assertTrue(StudentFeesJson.decode("[]").isEmpty())
        assertTrue(StudentFeesJson.decode("[{\"amount\":5}]").isEmpty())
    }
}
