package info.marozzo.tournament.desktop.application.stores.application

import androidx.compose.runtime.Immutable
import info.marozzo.tournament.desktop.application.stores.Timestamped
import info.marozzo.tournament.desktop.db.migrations.FileHistory
import info.marozzo.tournament.desktop.db.migrations.Settings
import info.marozzo.tournament.desktop.i18n.LanguageTag
import info.marozzo.tournament.desktop.theme.Theme
import kotlinx.collections.immutable.PersistentSet
import kotlinx.datetime.Instant

@Immutable
internal data class ApplicationState(
    val language: LanguageTag,
    val theme: Theme,
    val recent: PersistentSet<FileHistory>,
    override val timestamp: Instant
) : Timestamped

internal sealed interface ApplicationIntent

internal data class SetLanguage(val languageTag: LanguageTag) : ApplicationIntent

internal data class SetTheme(val theme: Theme) : ApplicationIntent
