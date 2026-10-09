package com.leoaristocrat.semesta.core.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.leoaristocrat.semesta.R
import com.leoaristocrat.semesta.core.design.components.SemestaLogoMark
import com.leoaristocrat.semesta.core.design.theme.SemestaTokens
import com.leoaristocrat.semesta.feature_user.domain.BottomBarStyle
import com.leoaristocrat.semesta.core.design.theme.LocalAppearancePreferences

/** Same routes and selection semantics on phones and larger workspaces. */
@Composable
internal fun SemestaPrimaryNavigation(
    selectedRoute: String,
    items: List<BottomNavItem>,
    pendingTasks: Int,
    rail: Boolean,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val prefs = LocalAppearancePreferences.current
    val labels = rail || prefs.bottomBarStyle == BottomBarStyle.LABELED
    val compactLabels = !rail && (LocalConfiguration.current.screenWidthDp < 360 || LocalDensity.current.fontScale > 1.2f)
    @Composable fun itemIcon(item: BottomNavItem, selected: Boolean) {
        val label = if (item.labelResId != 0) stringResource(item.labelResId) else item.label
        BadgedBox(badge = {
            if (prefs.tabBadges && item.route == AppRoutes.Academic && pendingTasks > 0) {
                Badge { Text(if (pendingTasks > 99) "99+" else pendingTasks.toString()) }
            }
        }) {
            Icon(item.iconFor(selected), contentDescription = if (labels) null else label,
                modifier = Modifier.size(SemestaTokens.Icon))
        }
    }
    @Composable fun itemLabel(item: BottomNavItem) {
        val fullLabel = if (item.labelResId != 0) stringResource(item.labelResId) else item.label
        val shownLabel = if (compactLabels) when (item.route) {
            AppRoutes.Calendar -> stringResource(R.string.semesta_nav_plan)
            AppRoutes.Expenses -> stringResource(R.string.semesta_nav_money)
            AppRoutes.Settings -> stringResource(R.string.semesta_nav_more)
            else -> fullLabel
        } else fullLabel
        Text(shownLabel, modifier = Modifier.clearAndSetSemantics { text = AnnotatedString(fullLabel) },
            style = MaterialTheme.typography.labelSmall, maxLines = 1, softWrap = false, overflow = TextOverflow.Ellipsis)
    }
    if (rail) {
        NavigationRail(modifier.width(SemestaTokens.RailWidth), containerColor = MaterialTheme.colorScheme.surface,
            header = { SemestaLogoMark(Modifier.padding(vertical = 20.dp), 32.dp) }) {
            Spacer(Modifier.height(24.dp))
            items.forEach { item ->
                val selected = selectedRoute == item.route
                NavigationRailItem(selected, { onNavigate(item.route) },
                    icon = { itemIcon(item, selected) }, label = { itemLabel(item) })
            }
        }
    } else {
        NavigationBar(modifier, containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 0.dp) {
            items.forEach { item ->
                val selected = selectedRoute == item.route
                NavigationBarItem(selected, { onNavigate(item.route) }, icon = { itemIcon(item, selected) },
                    label = if (labels) ({ itemLabel(item) }) else null)
            }
        }
    }
}
