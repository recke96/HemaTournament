package info.marozzo.tournament.desktop


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import info.marozzo.tournament.core.matchgenerators.SingleEliminationTournamentMatchGenerator
import info.marozzo.tournament.desktop.components.Select
import kotlinx.collections.immutable.persistentListOf

@Composable
fun Settings(modifier: Modifier = Modifier): Unit = Surface(modifier = modifier.fillMaxSize()) {
    val generators = remember { persistentListOf(SingleEliminationTournamentMatchGenerator(), RoundRobinTournamentMatchGenerator()) }
    val (generator, setGenerator) = remember { mutableStateOf(generators.first()) }

    Column(modifier = modifier.fillMaxSize()) {
        Select(
            value = generator,
            onValueChanged = { setGenerator(it) },
            options = generators,
            renderValue = { when(it) {
                is SingleEliminationTournamentMatchGenerator -> Text("Single Elimination")
                is RoundRobinTournamentMatchGenerator -> Text("Round-Robin")
            } },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
