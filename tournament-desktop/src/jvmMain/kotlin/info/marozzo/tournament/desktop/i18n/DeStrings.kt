package info.marozzo.tournament.desktop.i18n

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = Locales.DE)
internal val DeAppStrings = object : AppStrings {
    override val appTitle = "Turnier Planer"
    override val openNavigation = "Navigation öffnen"
    override val common = object : Common {
        override val ok = "Ok"
        override val cancel = "Abbrechen"
    }
    override val closeDialog = object : CloseDialog {
        override val title = "$appTitle schließen?"
        override val text = "Willst du die Anwendung wirklich schließen?"
    }
}
