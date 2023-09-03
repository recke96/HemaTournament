package info.marozzo.tournament.desktop.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import info.marozzo.tournament.desktop.application.stores.AcceptFunction
import info.marozzo.tournament.desktop.application.stores.tournament.NoEventState

@Composable
internal fun NoEventScreen(accept: AcceptFunction) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(text = "Hello World!", style = MaterialTheme.typography.titleLarge)
    }
}
