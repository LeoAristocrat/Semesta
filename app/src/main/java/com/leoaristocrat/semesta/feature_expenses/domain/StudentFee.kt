package com.leoaristocrat.semesta.feature_expenses.domain

import java.time.LocalDate

/** A personal fee obligation, in whole Indian rupees. Never an institution's quoted fee. */
data class StudentFee(
    val id: String,
    val name: String,
    val amount: Int,
    val paid: Int,
    val dueEpochDay: Long
) {
    val remaining: Int get() = (amount - paid).coerceAtLeast(0)
    fun overdue(today: LocalDate): Boolean = remaining > 0 && dueEpochDay < today.toEpochDay()
    val valid: Boolean get() = id.isNotBlank() && name.isNotBlank() && name.length <= 100 &&
        amount in 1..1_000_000_000 && paid in 0..amount &&
        dueEpochDay in LocalDate.of(1900, 1, 1).toEpochDay()..LocalDate.of(2200, 12, 31).toEpochDay()
}

/** Only recorded present/absent classes count; unmarked classes never imply attendance. */
object AttendanceRequirement {
    fun classesNeeded(present: Int, absent: Int, threshold: Int): Int? {
        require(present >= 0 && absent >= 0 && threshold in 1..100)
        if (threshold == 100) return if (absent == 0) 0 else null
        return kotlin.math.ceil(((threshold * (present.toDouble() + absent) - 100.0 * present) /
            (100 - threshold)).coerceAtLeast(0.0)).toInt()
    }
    fun classesCanMiss(present: Int, absent: Int, threshold: Int): Int {
        require(present >= 0 && absent >= 0 && threshold in 1..100)
        return kotlin.math.floor((100.0 * present / threshold - present - absent).coerceAtLeast(0.0)).toInt()
    }
}
