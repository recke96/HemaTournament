package info.marozzo.tournament.desktop.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import info.marozzo.tournament.desktop.application.stores.application.ApplicationIntent
import info.marozzo.tournament.desktop.application.stores.application.ApplicationState

@Composable
internal fun SettingsScreen(state: ApplicationState, accept: (ApplicationIntent) -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Hello settings", style = MaterialTheme.typography.displayLarge)
    }
}
