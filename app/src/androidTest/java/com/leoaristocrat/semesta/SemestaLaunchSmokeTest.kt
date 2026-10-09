package com.leoaristocrat.semesta

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SemestaLaunchSmokeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appLaunchesToSetupOrMainShell() {
        composeRule.waitUntil(timeoutMillis = 45_000) {
            composeRule.hasAnyText("Semesta") ||
                composeRule.hasAnyText("Your day, in focus.") ||
                composeRule.hasAnyText("Configura Semesta", substring = true) ||
                composeRule.hasAnyText("Hoy,", substring = true)
        }
    }
}

private fun androidx.compose.ui.test.junit4.ComposeTestRule.hasAnyText(
    text: String,
    substring: Boolean = false
): Boolean {
    return try {
        onAllNodes(hasText(text, substring = substring), useUnmergedTree = true)
            .fetchSemanticsNodes()
            .isNotEmpty()
    } catch (_: IllegalStateException) {
        false
    }
}
