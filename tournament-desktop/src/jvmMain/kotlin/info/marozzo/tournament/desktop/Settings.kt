package info.marozzo.tournament.desktop


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import info.marozzo.tournament.core.matchgenerators.SingleEliminationTournamentMatchGenerator
import info.marozzo.tournament.desktop.components.Select
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun Settings(modifier: Modifier = Modifier): Unit = Surface(modifier = modifier.fillMaxSize()) {
    val generators =
        remember { persistentListOf(SingleEliminationTournamentMatchGenerator(), RoundRobinTournamentMatchGenerator()) }
    val (generator, setGenerator) = remember { mutableStateOf(generators.first()) }

    val (currentText, setCurrentText) = remember { mutableStateOf("") }
    val (participants, setParticipants) = remember { mutableStateOf(persistentListOf<Participant>()) }
    fun addParticipant() {
        if (currentText.isNotBlank()) {
            setParticipants(participants.add(0, Participant(currentText)))
            setCurrentText("")
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Select(
            value = generator,
            onValueChanged = { setGenerator(it) },
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
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions { addParticipant() },
        )
        // TODO: Add scrollbar: https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Desktop_Components
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(participants) { participant ->
                ListItem() {
                    Text(participant.name)
                }
                Divider()
            }
        }
    }
}
