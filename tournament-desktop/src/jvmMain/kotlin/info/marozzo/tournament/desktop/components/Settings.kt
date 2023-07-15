package info.marozzo.tournament.desktop.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import info.marozzo.tournament.core.matchgenerators.SingleEliminationTournamentMatchGenerator
import info.marozzo.tournament.desktop.TournamentStore
import info.marozzo.tournament.desktop.components.util.Select
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun Settings(
    generator: MatchGenerator,
    participants: ImmutableList<Participant>,
    accept: (TournamentStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    val generators =
        remember { persistentListOf(SingleEliminationTournamentMatchGenerator(), RoundRobinTournamentMatchGenerator()) }

    val (currentText, setCurrentText) = remember { mutableStateOf("") }
    val isValid by remember(currentText, participants) {
        derivedStateOf {
            currentText.isNotBlank() && !participants.any { it.name == currentText }
        }
    }

    fun addParticipant() {
        if (isValid) {
            accept(TournamentStore.Intent.AddParticipant(currentText))
            setCurrentText("")
        }
    }

    Column(modifier = modifier) {
        Select(
            value = generator,
            onValueChanged = { accept(TournamentStore.Intent.ChangeMatchGenerator(it)) },
            options = generators,
            renderValue = {
                when (it) {
                    is SingleEliminationTournamentMatchGenerator -> Text("Single Elimination")
                    is RoundRobinTournamentMatchGenerator -> Text("Round-Robin")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = currentText,
            onValueChange = { value: String -> setCurrentText(value) },
            modifier = Modifier.fillMaxWidth().onKeyEvent {
                if (it.key == Key.Enter) {
                    addParticipant()
                    true
                } else false
            },
            label = { Text("Participant") },
            isError = !isValid,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions { addParticipant() },
        )
        // TODO: Add scrollbar: https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Desktop_Components
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(participants, key = { it.name }) { participant ->
                ListItem(trailing = {
                    IconButton(onClick = { accept(TournamentStore.Intent.RemoveParticipant(participant)) }) {
                        Icon(Icons.Default.Close, contentDescription = "Delete")
                    }
                }) {
                    Text(participant.name)
                }
                Divider()
            }
        }
    }
}
