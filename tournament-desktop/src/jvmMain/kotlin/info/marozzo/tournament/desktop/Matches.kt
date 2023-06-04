package info.marozzo.tournament.desktop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import info.marozzo.tournament.core.Competitor
import info.marozzo.tournament.core.Match
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Matches(
    generator: MatchGenerator,
    participants: ImmutableList<Participant>,
    modifier: Modifier = Modifier,
) = Surface(modifier = modifier.fillMaxSize()) {
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }
    val (matches, setMatches) = remember { mutableStateOf<ImmutableList<Match>>(persistentListOf()) }

    fun Competitor.text(): String = when (this) {
        is Competitor.Fixed -> this.participant.name
        is Competitor.WinnerOf -> "Winner of Match ${matches.indexOf(this.match) + 1}"
        is Competitor.LoserOf -> "Loser of Match ${matches.indexOf(this.match) + 1}"
    }

    LaunchedEffect(generator, participants) {
        setIsLoading(true)
        setMatches(generator.generate(participants))
        setIsLoading(false)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text("Matches", style = MaterialTheme.typography.h4, modifier = Modifier.fillMaxWidth())
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(matches) { idx, match ->
                ListItem {
                    Text("${idx + 1}. ${match.a.text()} vs. ${match.b.text()}")
                }
                Divider()
            }
        }
    }
}
