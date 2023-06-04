package info.marozzo.tournament.desktop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.*

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold(
            topBar = { TournamentPlannerAppBar() }
        ) { padding ->
            BoxWithConstraints(modifier = Modifier.padding(padding)) {

            }
        }
    }
}

fun main() = application {
    Window(
        title = "Tournament Planner",
        onCloseRequest = ::exitApplication) {
        App()
    }
}
