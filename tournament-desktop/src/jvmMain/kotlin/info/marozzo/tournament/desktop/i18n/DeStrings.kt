package info.marozzo.tournament.desktop.i18n

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = DeStrings.TAG)
internal val DeAppStrings : AppStrings = DeStrings

private object DeStrings : AppStrings {
    const val TAG = "de"

    override val appTitle = "Turnier Planer"
    override val common = object : Common {
        override val ok = "Ok"
        override val cancel = "Abbrechen"
    }
    override val closeDialog = object : CloseDialog {
        override val title = "$appTitle schließen?"
        override val text = "Willst du die Anwendung wirklich schließen?"
    }
}
