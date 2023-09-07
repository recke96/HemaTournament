package info.marozzo.tournament.desktop.application.stores

import kotlinx.datetime.Instant

interface Timestamped {
    val timestamp: Instant
}
