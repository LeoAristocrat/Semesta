package com.leoaristocrat.semesta

import android.graphics.Bitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.leoaristocrat.semesta.feature_user.domain.*
import com.leoaristocrat.semesta.feature_grades.domain.Subject
import com.leoaristocrat.semesta.feature_notes.domain.QuickNote
import com.leoaristocrat.semesta.feature_tasks.domain.*
import com.leoaristocrat.semesta.feature_schedule.domain.ClassSession
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.time.LocalDate

/** Runs on a disposable review emulator. Fixture data never ships in the product. */
@RunWith(AndroidJUnit4::class)
class SemestaAcceptanceTest {
    @get:Rule val rule = createAndroidComposeRule<MainActivity>()

    @Test fun realRepositoriesPrimaryNavigationAndActivityRecreation() {
        val app = rule.activity.application as SemestaApplication
        rule.waitUntil(20_000) { app.userRepository.didLoad }
        val now = System.currentTimeMillis()
        app.userRepository.saveUserProfile(UserProfile(
            userId = app.userRepository.currentUser.value.userId,
            preferredName = "Arjun", careerOrProgram = "B.Tech / B.E. — Computer Science", studyArea = StudyArea.ENGINEERING_TECHNOLOGY,
            gradingScale = GradingScale.ZERO_TO_TEN, passingGrade = 4.0, targetAverage = 8.0,
            minimumAttendancePercent = 85,
            feePlans = listOf(com.leoaristocrat.semesta.feature_expenses.domain.StudentFee("review-fee", "Semester 1 tuition", 75000, 25000, LocalDate.now().plusDays(7).toEpochDay())),
            enabledModules = AppModule.entries.toSet(), setupCompleted = true,
            visualPreference = VisualPreference.LIGHT,
            appearancePreferences = AppearancePreferences.defaults().copy(motionPreference = MotionPreference.REDUCED),
            createdAt = now, updatedAt = now
        ))
        app.gradesRepository.addSubject(Subject("review-math", "Applied mathematics", 8.0, emptyList()))
        app.gradesRepository.addSubject(Subject("review-design", "Design methods", 8.0, emptyList()))
        app.tasksRepository.addTask(StudentTask("review-task", "Prototype review", "Prepare usability findings", "review-design",
            TaskType.PROJECT, now + 86_400_000, TaskDifficulty.MEDIUM, 45, false, now, now))
        app.notesRepository.addNote(QuickNote("review-note", title = "A good question changes the answer",
            body = "Research prompts, usability observations and the next iteration.", subjectId = "review-design", pinned = true,
            createdAt = now, updatedAt = now))
        app.scheduleRepository.saveSession(ClassSession("review-class", "review-math", setOf(LocalDate.now().dayOfWeek.value),
            9 * 60, 10 * 60 + 30, "Room 3 • Prof. Sharma", createdAt = now, updatedAt = now))

        waitText("Arjun", substring = true)
        waitText("Your day, in focus.")
        screenshot("dashboard-light")
        tap("Study")
        waitText("Applied mathematics")
        screenshot("study-light")
        tap("Schedule")
        screenshot("schedule-light")
        tap("Expenses")
        screenshot("expenses-light")
        tap("Track fees and instalments")
        waitText("Semester 1 tuition")
        tap("Edit fee or record payment")
        rule.onNode(hasSetTextAction() and hasText("Amount already paid (₹)")).performTextReplacement("80000")
        tap("Save")
        waitText("Enter a name, a valid due date and whole rupee amounts. Paid must be between zero and the total.")
        screenshot("fee-validation")
        rule.onNode(hasSetTextAction() and hasText("Amount already paid (₹)")).performTextReplacement("40000")
        tap("Save")
        rule.waitUntil(15_000) { app.userRepository.userProfile.value?.feePlans?.single()?.paid == 40000 }
        waitText("Paid ₹40,000 of ₹75,000")
        screenshot("fees-light")
        rule.activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
        rule.waitForIdle()
        tap("Settings")
        screenshot("settings-light")
        tap("Today")
        app.userRepository.userProfile.value!!.let {
            app.userRepository.saveUserProfile(it.copy(visualPreference = VisualPreference.DARK))
        }
        rule.waitUntil(10_000) { app.userRepository.userProfile.value?.visualPreference == VisualPreference.DARK }
        rule.activityRule.scenario.recreate()
        waitText("Arjun", substring = true)
        waitText("Your day, in focus.")
        screenshot("dashboard-dark")
        tap("Study")
        waitText("Applied mathematics")
        screenshot("study-dark")
        rule.runOnIdle {
            check(app.userRepository.userProfile.value?.feePlans?.single()?.paid == 40000)
            check(app.userRepository.userProfile.value?.minimumAttendancePercent == 85)
            check(app.tasksRepository.tasks.value.any { it.id == "review-task" })
            check(app.notesRepository.notes.value.any { it.id == "review-note" })
        }
    }

    private fun waitText(text: String, substring: Boolean = false) = rule.waitUntil(30_000) {
        rule.onAllNodes(hasText(text, substring = substring), useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
    }
    private fun tap(text: String) {
        rule.onAllNodes(hasText(text) and hasClickAction(), useUnmergedTree = false)[0].performClick()
        rule.waitForIdle()
    }
    private fun screenshot(name: String) {
        rule.waitForIdle()
        val bitmap = InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot()
        val folder = File(rule.activity.getExternalFilesDir(null), "review").apply { mkdirs() }
        File(folder, "$name.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        val resolver = rule.activity.contentResolver
        val values = android.content.ContentValues().apply {
            put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "semesta-$name.png")
            put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(android.provider.MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SemestaReview")
        }
        val uri = resolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        resolver.openOutputStream(uri)!!.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
    }
}
