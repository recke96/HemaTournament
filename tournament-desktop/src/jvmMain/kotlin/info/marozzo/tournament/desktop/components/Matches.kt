package info.marozzo.tournament.desktop.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import info.marozzo.tournament.core.Competitor
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.Round
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Matches(
    generator: MatchGenerator,
    participants: ImmutableList<Participant>,
    modifier: Modifier = Modifier,
) = Surface(modifier = modifier, color = MaterialTheme.colors.primarySurface) {
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }
    val (rounds, setRounds) = remember { mutableStateOf<ImmutableList<Round>>(persistentListOf()) }

    fun Competitor.text(rounds: List<Round>): String = when (this) {
        is Competitor.Fixed -> this.participant.name
        is Competitor.WinnerOf -> "Winner of Round ${rounds.find { it.matches.contains(match) }?.rank}, Match ${match.rank}"
        is Competitor.LoserOf -> "Loser of Round ${rounds.find { it.matches.contains(match) }?.rank}, Match ${match.rank}"
    }

    LaunchedEffect(generator, participants) {
        setIsLoading(true)
        setRounds(generator.generate(participants))
        setIsLoading(false)
    }

    Column {
        AnimatedVisibility(visible = isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            rounds.forEach { round ->
                stickyHeader(key = "r${round.rank}", contentType = "header") {
                    ListItem { Text("Round ${round.rank}") }
                }
                items(round.matches, key = { "r${round.rank}m${it.rank}" }, contentType = { "match" }) {
                    ListItem {
                        Text("${it.rank}. ${it.a.text(rounds)} vs. ${it.b.text(rounds)}")
                    }
                }
            }
        }
    }
}
