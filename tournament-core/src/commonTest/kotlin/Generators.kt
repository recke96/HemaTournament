import info.marozzo.tournament.core.Participant
import io.kotest.property.Arb
import io.kotest.property.arbitrary.map
import io.kotest.property.arbs.name

fun Arb.Companion.participant() = Arb.name().map { Participant("${it.first.name} ${it.last.name}") }
