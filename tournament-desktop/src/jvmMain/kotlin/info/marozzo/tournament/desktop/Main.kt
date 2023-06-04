package info.marozzo.tournament.desktop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
@Preview
fun App() {
    val splitPaneState = rememberSplitPaneState(initialPositionPercentage = 0.4f)

    MaterialTheme {
        Scaffold(
            topBar = { TournamentPlannerAppBar() }
        ) { padding ->
            HorizontalSplitPane(modifier = Modifier.padding(padding), splitPaneState = splitPaneState) {
                first(minSize = 300.dp) {
                    Surface(modifier = Modifier.fillMaxSize()){
                        Text("First")
                    }
                }
                splitter { visiblePart { Box(modifier = Modifier.background(Color.Black).fillMaxHeight().width(2.dp)) } }
                second(minSize = 300.dp) {
                    Surface(modifier = Modifier.fillMaxSize()){
                        Text("Second")
                    }
                }
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
