package info.marozzo.tournament.desktop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import info.marozzo.tournament.desktop.components.App
import info.marozzo.tournament.desktop.components.util.WithWidthClass


@OptIn(ExperimentalMaterialApi::class)
fun main() = application {
    val windowState = rememberWindowState(
        placement = WindowPlacement.Maximized, size = DpSize.Unspecified
    )
    val (isCloseRequested, setIsCloseRequested) = remember { mutableStateOf(false) }

    Window(
        title = "Tournament Planner",
        state = windowState,
        onCloseRequest = { setIsCloseRequested(true) },
    ) {
        WithWidthClass {

            App()

            AnimatedVisibility(visible = isCloseRequested) {
                val focusRequester = remember { FocusRequester() }
                AlertDialog(
                    onDismissRequest = { /* Ignore since the user should explicitly cancel or confirm */ },
                    title = { Text("Close Tournament Planner?") },
                    text = { Text("Are you sure you want to close the application?") },
                    confirmButton = {
                        Button(
                            onClick = { this@application.exitApplication() },
                            modifier = Modifier.focusRequester(focusRequester)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { setIsCloseRequested(false) }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Text("Cancel")
                        }
                    }
                )

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        }
    }
}
