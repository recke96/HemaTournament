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
internal sealed interface ApplicationState : Timestamped

@Immutable
internal data class InitializingState(override val timestamp: Instant) : ApplicationState

@Immutable
internal data class SettingsState(
    val settingsVersion: Settings.Version,
    val language: LanguageTag,
    val theme: Theme,
    val recent: PersistentSet<FileHistory>,
    override val timestamp: Instant
) : ApplicationState

internal sealed interface ApplicationIntent

internal data class SetLanguage(val languageTag: LanguageTag) : ApplicationIntent

internal data class SetTheme(val theme: Theme) : ApplicationIntent
