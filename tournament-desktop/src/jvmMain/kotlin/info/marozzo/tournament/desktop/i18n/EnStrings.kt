package info.marozzo.tournament.desktop.i18n

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = Locales.EN, default = true)
internal val EnAppStrings = object : AppStrings {
    override val appTitle = "Tournament Planner"
    override val openNavigation = "Open navigation"
    override val common = object : Common {
        override val ok = "Ok"
        override val cancel = "Cancel"
    }
    override val closeDialog = object : CloseDialog {
        override val title = "Close $appTitle?"
        override val text = "Do you really want to close the application?"
    }
}
