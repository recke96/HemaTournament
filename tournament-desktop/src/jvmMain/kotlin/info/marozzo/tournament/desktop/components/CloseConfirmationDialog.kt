package info.marozzo.tournament.desktop.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
fun CloseConfirmationDialog(
    isCloseRequested: Boolean,
    onClose: () -> Unit = {},
    onDismiss: () -> Unit = {}
) = AnimatedVisibility(visible = isCloseRequested) {
    val focusRequester = remember { FocusRequester() }
    AlertDialog(
        onDismissRequest = { /* Ignore since the user should explicitly cancel or confirm */ },
        title = { Text("Close Tournament Planner?") },
        text = { Text("Are you sure you want to close the application?") },
        confirmButton = {
            Button(
                onClick = { onClose() },
                modifier = Modifier.focusRequester(focusRequester)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Text("Ok")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onDismiss() }) {
                Icon(Icons.Default.Close, contentDescription = null)
                Text("Cancel")
            }
        }
    )

    LaunchedEffect(isCloseRequested) {
        if (isCloseRequested) {
            focusRequester.requestFocus()
        }
    }
}
