package com.leoaristocrat.semesta.feature_home.domain

import com.leoaristocrat.semesta.feature_expenses.domain.Expense
import com.leoaristocrat.semesta.feature_grades.domain.Subject
import com.leoaristocrat.semesta.feature_notes.domain.QuickNote
import com.leoaristocrat.semesta.feature_schedule.domain.ClassOccurrence
import com.leoaristocrat.semesta.feature_schedule.domain.ClassSession
import com.leoaristocrat.semesta.feature_tasks.domain.StudentTask
import com.leoaristocrat.semesta.feature_templates.domain.AcademicWork

data class HomeContent(
    val subjects: List<Subject>,
    val tasks: List<StudentTask>,
    val expenses: List<Expense>,
    val works: List<AcademicWork>,
    val classSessions: List<ClassSession> = emptyList(),
    /** Lo marcado clase a clase, para el bloque de Asistencia. */
    val occurrences: List<ClassOccurrence> = emptyList(),
    /** Las notas rápidas, de las que Inicio solo enseña las fijadas. */
    val notes: List<QuickNote> = emptyList()
)
