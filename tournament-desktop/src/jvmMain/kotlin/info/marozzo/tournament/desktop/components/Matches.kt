package info.marozzo.tournament.desktop.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.marozzo.tournament.core.*
import info.marozzo.tournament.desktop.TournamentStore
import info.marozzo.tournament.desktop.components.util.Responsive
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun Matches(
    rounds: ImmutableList<Round>,
    results: ImmutableMap<Match, MatchResult<*>?>,
    accept: (TournamentStore.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }
    val (matchToBeScored, setMatchToBeScored) = remember { mutableStateOf<Match?>(null) }

    fun Competitor.text(rounds: List<Round>): String = when (this) {
        is Competitor.Fixed -> this.participant.name
        is Competitor.WinnerOf -> "Winner of Round ${rounds.find { it.matches.contains(match) }?.rank}, Match ${match.rank}"
        is Competitor.LoserOf -> "Loser of Round ${rounds.find { it.matches.contains(match) }?.rank}, Match ${match.rank}"
    }

    Column(modifier = modifier) {
        AnimatedVisibility(visible = isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            rounds.forEach { round ->
                stickyHeader(key = "r${round.rank}", contentType = "header") {
                    ListItem { Text("Round ${round.rank}") }
                }
                items(round.matches, key = { "r${round.rank}m${it.rank}" }, contentType = { "match" }) {
                    ListItem(
                        secondaryText = { results[it]?.let { Text("${it.a} : ${it.b}") } },
                        singleLineSecondaryText = true,
                        trailing = {
                            IconButton(onClick = { setMatchToBeScored(it) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit the score")
                            }
                        }) {
                        Text("${it.rank}. ${it.a.text(rounds)} vs. ${it.b.text(rounds)}")
                    }
                }
            }
        }
    }

    if (matchToBeScored != null) {
        val (rawPointsA, setRawPointsA) = remember {
            mutableStateOf(
                results[matchToBeScored]?.a?.toString() ?: ""
            )
        }
        val pointsA by remember(rawPointsA) { derivedStateOf { rawPointsA.toIntOrNull()?.let { Points(it) } } }

        val (rawPointsB, setRawPointsB) = remember {
            mutableStateOf(
                results[matchToBeScored]?.a?.toString() ?: ""
            )
        }
        val pointsB by remember(rawPointsB) { derivedStateOf { rawPointsB.toIntOrNull()?.let { Points(it) } } }

        val (rawDoubles, setRawDoubles) = remember { mutableStateOf("0") }
        val doubles by remember(rawDoubles) { derivedStateOf { rawDoubles.toIntOrNull()?.let { Hits(it) } } }

        @Composable
        fun ScoringDialogButtons() {
            OutlinedButton(onClick = { setMatchToBeScored(null) }) { Text("Cancel") }
            Spacer(modifier = Modifier.widthIn(5.dp, 10.dp))
            Button(
                enabled = pointsA != null && pointsB != null && doubles != null,
                onClick = {
                    accept(TournamentStore.Intent.EnterResult(CutScoreResult(matchToBeScored, pointsA!!, pointsB!!, doubles!!)))
                    setMatchToBeScored(null)
                }) {
                Text("OK")
            }
        }

        AlertDialog(
            onDismissRequest = { setMatchToBeScored(null) },
            title = { Text("Score Round ${rounds.find { it.matches.contains(matchToBeScored) }?.rank} Match ${matchToBeScored.rank}") },
            text = {
                Column {
                    Text("${matchToBeScored.a.text(rounds)} vs. ${matchToBeScored.b.text(rounds)}")
                    TextField(
                        rawPointsA,
                        onValueChange = { setRawPointsA(it) },
                        isError = pointsA == null,
                        singleLine = true,
                        label = { Text("Points A") }
                    )
                    TextField(
                        rawPointsB,
                        onValueChange = { setRawPointsB(it) },
                        isError = pointsB == null,
                        singleLine = true,
                        label = { Text("Points B") }
                    )
                    TextField(
                        rawDoubles,
                        onValueChange = { setRawDoubles(it) },
                        isError = doubles == null,
                        singleLine = true,
                        label = { Text("Double Hits") }
                    )
                }
            },
            buttons = {
                Responsive(
                    compact = { Column(modifier = Modifier.padding(5.dp).fillMaxWidth(), horizontalAlignment = Alignment.End) { ScoringDialogButtons() } },
                    expanded = {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        ) { ScoringDialogButtons() }
                    }
                )
            }
        )
    }
}
