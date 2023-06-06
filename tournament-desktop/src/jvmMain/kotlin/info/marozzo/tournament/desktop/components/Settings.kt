package info.marozzo.tournament.desktop.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import info.marozzo.tournament.desktop.components.util.Select
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun Settings(
    generator: MatchGenerator,
    onGeneratorChanged: (MatchGenerator) -> Unit,
    participants: ImmutableList<Participant>,
    onParticipantAdd: (Participant) -> Unit,
    onParticipantRemove: (Participant) -> Unit,
    modifier: Modifier = Modifier
): Unit = Surface(modifier = modifier.fillMaxSize()) {
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
            onParticipantAdd(Participant(currentText.trim()))
            setCurrentText("")
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text("Settings", style = MaterialTheme.typography.h4, modifier = Modifier.fillMaxWidth())
        Select(value = generator, onValueChanged = { onGeneratorChanged(it) }, options = generators, renderValue = {
            when (it) {
                is SingleEliminationTournamentMatchGenerator -> Text("Single Elimination")
                is RoundRobinTournamentMatchGenerator -> Text("Round-Robin")
            }
        }, modifier = Modifier.fillMaxWidth()
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
                    IconButton(onClick = { onParticipantRemove(participant) }) {
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
