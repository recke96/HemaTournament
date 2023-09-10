package info.marozzo.tournament.desktop

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import info.marozzo.tournament.desktop.application.stores.application.ApplicationIntent
import info.marozzo.tournament.desktop.application.stores.application.ApplicationState
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentIntent
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentState
import info.marozzo.tournament.desktop.components.Navigation
import info.marozzo.tournament.desktop.components.navigationTransform
import info.marozzo.tournament.desktop.screens.NoEventScreen
import info.marozzo.tournament.desktop.screens.Screen
import info.marozzo.tournament.desktop.screens.SettingsScreen

@Composable
internal fun TournamentApp(
    application: ApplicationState,
    acceptApplication: (ApplicationIntent) -> Unit,
    tournament: TournamentState,
    acceptTournament: (TournamentIntent) -> Unit
) =
    Scaffold { padding ->
        Row {
            Navigation(application, acceptApplication)
            Box(Modifier.padding(padding)) {
                AnimatedContent(
                    targetState = application.screen,
                    contentAlignment = Alignment.TopCenter,
                    transitionSpec = AnimatedContentTransitionScope<Screen>::navigationTransform
                ) { targetScreen ->
                    when (targetScreen) {
                        Screen.HOME -> NoEventScreen(acceptTournament)
                        Screen.SETTINGS -> SettingsScreen(application, acceptApplication)
                    }
                }
            }
        }
    }
