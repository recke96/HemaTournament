package info.marozzo.tournament.desktop.i18n

import cafe.adriel.lyricist.LyricistStrings
import info.marozzo.tournament.desktop.theme.Theme

@LyricistStrings(languageTag = DeStrings.TAG)
internal val DeAppStrings: AppStrings = DeStrings

private object DeStrings : AppStrings {
    const val TAG = "de"

    override val appTitle = "Turnier Planer"

    override val common = object : AppStrings.Common {
        override val ok = "Ok"
        override val cancel = "Abbrechen"
    }

    override val navigation = object : AppStrings.Navigation {
        override val home: String = "Home"
        override val settings: String = "Einstellungen"
    }

    override val settings = object : AppStrings.Settings {
        override val title = "Einstellungen"
        override val languageLabel = "Sprache"
        override val languageText = { tag: LanguageTag ->
            when (tag) {
                LanguageTag.SYSTEM -> "Systemsprache"
                LanguageTag.DE -> "Deutsch"
                LanguageTag.EN -> "Englisch"
            }
        }
        override val themeLabel = "Theme"
        override val themeText = { theme: Theme ->
            when (theme) {
                Theme.SYSTEM -> "System"
                Theme.DARK -> "Dunkel"
                Theme.LIGHT -> "Hell"
            }
        }
    }

    override val closeDialog = object : AppStrings.CloseDialog {
        override val title = "$appTitle schließen?"
        override val text = "Willst du die Anwendung wirklich schließen?"
    }
}
