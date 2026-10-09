package com.leoaristocrat.semesta

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leoaristocrat.semesta.core.design.theme.SemestaTheme
import com.leoaristocrat.semesta.feature_home.domain.DailyFocusItem
import com.leoaristocrat.semesta.feature_home.domain.HomePriorityAction
import com.leoaristocrat.semesta.feature_home.domain.HomePrioritySummary
import com.leoaristocrat.semesta.feature_home.domain.HomeSummary
import com.leoaristocrat.semesta.feature_home.presentation.HomeScreen
import com.leoaristocrat.semesta.feature_home.presentation.HomeUiState
import com.leoaristocrat.semesta.feature_user.domain.AppModule
import com.leoaristocrat.semesta.feature_user.domain.GradingScale
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeDailyFocusPanelTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun homeShowsPriorityAndDispatchesSubjectAction() {
        var openedRoute = ""

        composeRule.setContent {
            SemestaTheme(darkTheme = false) {
                HomeScreen(
                    uiState = HomeUiState(summary = dailyFocusSummary()),
                    onAddSubjectClick = {},
                    onSeeAllSubjectsClick = { openedRoute = "subjects" },
                    onSeeTasksClick = { openedRoute = "tasks" },
                    onSeeExpensesClick = { openedRoute = "expenses" },
                    onOpenTemplatesClick = { openedRoute = "templates" },
                    onSubjectClick = { openedRoute = "subject:$it" },
                    onProfileClick = {},
                    onAddGradeClick = {},
                    onAddTaskClick = {},
                    onAddExpenseClick = {}
                )
            }
        }

        composeRule.onNodeWithText("Física necesita atención").assertIsDisplayed()
        val context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        composeRule.onNodeWithText(context.getString(R.string.home_view_subject)).performTouchInput { click() }

        composeRule.runOnIdle {
            assertEquals("subject:physics", openedRoute)
        }
    }

    private fun dailyFocusSummary(): HomeSummary {
        return HomeSummary(
            userName = "QA",
            avatarPhotoUrl = null,
            dashboardMessage = "Física necesita atención hoy.",
            priority = HomePrioritySummary(
                title = "Física necesita atención",
                shortDescription = "Repasa esta materia antes de abrir más frentes.",
                action = HomePriorityAction.SUBJECT,
                subjectId = "physics"
            ),
            dailyFocusItems = listOf(
                DailyFocusItem(
                    slotLabel = "Ahora",
                    title = "Repasar Física",
                    detail = "Revisa el tema con mayor peso.",
                    minutesText = "15 min",
                    actionLabel = "Abrir",
                    action = HomePriorityAction.SUBJECT,
                    subjectId = "physics"
                ),
                DailyFocusItem(
                    slotLabel = "Luego",
                    title = "Cerrar tarea corta",
                    detail = "Deja una entrega lista.",
                    minutesText = "20 min",
                    actionLabel = "Tareas",
                    action = HomePriorityAction.TASKS
                )
            ),
            generalAverage = 3.4,
            subjectsCount = 1,
            tasksToday = 1,
            overdueTasks = 0,
            pendingTasks = 2,
            openAcademicWorks = 0,
            subjects = emptyList(),
            riskSubject = null,
            neededGrade = null,
            nextTask = null,
            nextAcademicWork = null,
            todayItems = emptyList(),
            weeklyExpenses = null,
            weeklyExpenseTotal = 0,
            productivitySummary = "0 completadas · 2 pendientes",
            companionInsight = "Empieza por Física y luego cierra una tarea.",
            gradingScale = GradingScale.ZERO_TO_FIVE,
            enabledModules = setOf(AppModule.GRADES, AppModule.TASKS, AppModule.EXPENSES)
        )
    }
}
