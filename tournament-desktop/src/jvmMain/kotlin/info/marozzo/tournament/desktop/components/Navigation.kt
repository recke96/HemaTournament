package info.marozzo.tournament.desktop.components

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import info.marozzo.tournament.desktop.i18n.LocalStrings
import info.marozzo.tournament.desktop.application.stores.application.ApplicationIntent
import info.marozzo.tournament.desktop.application.stores.application.ApplicationState
import info.marozzo.tournament.desktop.application.stores.application.ChangeScreen
import info.marozzo.tournament.desktop.screens.Screen

@Composable
internal fun Navigation(state: ApplicationState, accept: (ApplicationIntent) -> Unit) {
    NavigationRail(
        header = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Screen.entries.forEach {
            NavigationRailItem(
                selected = it == state.screen,
                onClick = { accept(ChangeScreen(it)) },
                icon = {
                    Icon(
                        if (it == state.screen) it.iconSelected else it.icon,
                        contentDescription = it.label.invoke(LocalStrings.current)
                    )
                },
                label = { Text(it.label.invoke(LocalStrings.current)) },
            )
        }
    }
}

internal fun AnimatedContentTransitionScope<Screen>.navigationTransform(): ContentTransform {
    return if (targetState > initialState) {
        // target screen is below initial screen
        slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessHigh)) { h -> -h } + fadeIn(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        ) togetherWith slideOutVertically(animationSpec = spring(stiffness = Spring.StiffnessHigh)) { h -> h } + fadeOut(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        )
    } else {
        // target screen is above initial screen
        slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessHigh)) { h -> h } + fadeIn(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        ) togetherWith slideOutVertically(animationSpec = spring(stiffness = Spring.StiffnessHigh)) { h -> -h } + fadeOut(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
        )
    }.using(SizeTransform(clip = false))
}
